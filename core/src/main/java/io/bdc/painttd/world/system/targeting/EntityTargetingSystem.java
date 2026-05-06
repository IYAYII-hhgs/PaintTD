package io.bdc.painttd.world.system.targeting;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.family.targeting.*;
import io.bdc.painttd.world.family.turret.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class EntityTargetingSystem extends WorldSystem {
    private EntityTargetingStore entityTargetingStore;
    private TurretStore turretStore;
    private PositionStore positionStore;
    private EntityHealthStore healthStore;
    private EntityTeamStore teamStore;
    private TileBucketQueryAPI queryAPI;

    public EntityTargetingSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        entityTargetingStore = binder.getStore(EntityTargetingStore.class);
        turretStore = binder.getStore(TurretStore.class);
        positionStore = binder.getStore(PositionStore.class);
        healthStore = binder.getStore(EntityHealthStore.class);
        teamStore = binder.getStore(EntityTeamStore.class);
        queryAPI = binder.getApi(TileBucketQueryAPI.class);
    }

    @Override
    public void run(float delta) {
        var strategyItems = entityTargetingStore.strategyArray.items;
        var targetItems = entityTargetingStore.targetIdArray.items;

        var posXItems = positionStore.x.items;
        var posYItems = positionStore.y.items;
        var healthItems = healthStore.healths.items;
        var teamItems = teamStore.teams.items;

        for (int slot = 0; slot < entityTargetingStore.size(); slot++) {
            int eid = entityTargetingStore.eidOf(slot);
            int turretSlot = turretStore.slotOf(eid);
            if (turretSlot == -1) continue;

            TurretDef def = turretStore.defArray.get(turretSlot);
            if (def == null) continue;
            float range = def.range;

            int posSlot = positionStore.slotOf(eid);
            float cx = posXItems[posSlot];
            float cy = posYItems[posSlot];

            int strategy = strategyItems[slot];

            int bestEid = -1;
            float bestScore = 0;
            int eTeam = teamItems[teamStore.slotOf(eid)];

            var candidates = queryAPI.collectAabb(cx - range, cy - range, cx + range, cy + range);
            int[] cItems = candidates.items;
            int cSize = candidates.size;

            for (int i = 0; i < cSize; i++) {
                int ceid = cItems[i];
                if (ceid == eid) continue;

                int ceidSlot = healthStore.slotOf(ceid);
                if (ceidSlot == -1) continue;
                float hp = healthItems[ceidSlot];
                if (hp <= 0) continue;

                int ceidTeamSlot = teamStore.slotOf(ceid);
                if (ceidTeamSlot == -1) continue;
                if (teamItems[ceidTeamSlot] == eTeam) continue;

                switch (strategy) {
                    case 0: {
                        if (hp > bestScore) {
                            bestEid = ceid;
                            bestScore = hp;
                        }
                        break;
                    }
                    case 1: {
                        if (bestEid == -1 || hp < bestScore) {
                            bestEid = ceid;
                            bestScore = hp;
                        }
                        break;
                    }
                    case 2: {
                        int ceidPosSlot = positionStore.slotOf(ceid);
                        float dx = posXItems[ceidPosSlot] - cx;
                        float dy = posYItems[ceidPosSlot] - cy;
                        float distSq = dx * dx + dy * dy;
                        if (bestEid == -1 || distSq < bestScore) {
                            bestEid = ceid;
                            bestScore = distSq;
                        }
                        break;
                    }
                }
            }

            targetItems[slot] = bestEid;
        }
    }
}
