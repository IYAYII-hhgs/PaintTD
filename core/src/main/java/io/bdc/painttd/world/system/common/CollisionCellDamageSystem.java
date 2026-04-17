package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class CollisionCellDamageSystem extends WorldSystem {
    MapStore mapStore;
    EntityTeamStore entityTeamStore;
    MapAPI mapAPI;
    DamageAPI damageAPI;

    public CollisionCellDamageSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        var dispatchAPI = binder.getApi(CollisionDispatchAPI.class);
        dispatchAPI.onEcStain(this::handleEc);
        mapStore = binder.getStore(MapStore.class);
        entityTeamStore = binder.getStore(EntityTeamStore.class);
        mapAPI = binder.getApi(MapAPI.class);
        damageAPI = binder.getApi(DamageAPI.class);
    }

    @Override
    public void run(float delta) {
    }

    protected void handleEc(int eid, int cid) {
        float hp = mapStore.hpMask[cid];
        if (hp <= 0) return;
        int cellTeam = mapStore.teamMask[cid];
        int entityTeam = entityTeamStore.get(eid);
        if (cellTeam == entityTeam) return;
        float dmg = mapAPI.damageCell(0.5f, cid);
        if (dmg > 0) {
            damageAPI.realDamage(eid, dmg);
        }
    }
}
