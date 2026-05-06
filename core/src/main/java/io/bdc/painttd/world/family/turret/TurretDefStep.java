package io.bdc.painttd.world.family.turret;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.request.*;

public class TurretDefStep implements AssembleStep {
    public TurretDef turretDef;

    public TurretDefStep setup(TurretDef turretDef) {
        this.turretDef = turretDef;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        binder.getStore(TurretStore.class).createAndSet(eid, turretDef);
    }
}
