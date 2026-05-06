package io.bdc.painttd.world.family.weapon;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.family.turret.*;
import io.bdc.painttd.world.store.request.*;

public class WeaponStep implements AssembleStep {
    public WeaponDef def;

    public WeaponStep setup(WeaponDef def) {
        this.def = def;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var dependent = binder.getStore(TurretStore.class);
        if (!dependent.has(eid)) throw new IllegalStateException("TurretStore is missed for" + req.entityDef.name);

        var api = binder.getApi(ActTypeAPI.class);
        var store = binder.getStore(WeaponStore.class);
        store.createAndSet(eid, def, api.getOrCreate(def.attackAct));
    }
}
