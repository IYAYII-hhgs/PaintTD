package io.bdc.painttd.world.family.bullet;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.family.weapon.*;
import io.bdc.painttd.world.store.request.*;

public class PostBulletDamageStep implements PostSpawnStep {
    public BulletWeaponDef def;

    public PostBulletDamageStep setup(BulletWeaponDef def) {
        this.def = def;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        BulletDamageStore damageStore = binder.getStore(BulletDamageStore.class);
        damageStore.createAndSet(eid, def);
    }
}
