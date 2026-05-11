package io.bdc.painttd.world.family.weapon;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.request.*;

public class WeaponStep implements AssembleStep {
    public WeaponDef def;

    public WeaponStep setup(WeaponDef def) {
        this.def = def;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        // 委托实现
        def.onAssemble(eid, req, binder);
    }
}
