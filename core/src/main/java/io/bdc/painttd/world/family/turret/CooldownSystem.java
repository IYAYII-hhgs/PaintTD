package io.bdc.painttd.world.family.turret;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class CooldownSystem extends WorldSystem {
    EntityMetaStore entityMetaStore;
    TurretStore turretStore;

    public CooldownSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        turretStore = binder.getStore(TurretStore.class);
        entityMetaStore = binder.getStore(EntityMetaStore.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < turretStore.size(); slot++) {
            var cooldownArray = turretStore.cdArray.items;
            if (cooldownArray[slot] > 0) {
                cooldownArray[slot] -= 1;
            }
//            else {
//                int eid = turretStore.eidOf(slot);
//                var entityDef = entityMetaStore.get(eid);
//                if (entityDef.turretDef != null) {
//                    turretStore.cooldown.items[slot] = entityDef.turretDef.cooldown;
//                }
//            }
        }
    }
}
