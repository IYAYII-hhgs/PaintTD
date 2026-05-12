package io.bdc.painttd.world.family.bullet;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.family.weapon.*;
import io.bdc.painttd.world.store.request.*;

public class PostBulletLifeTimeStep implements PostSpawnStep {
    public float duration = 1f;

    public PostBulletLifeTimeStep duration(float amt) {
        this.duration = amt;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        BulletLifetimeStore damageStore = binder.getStore(BulletLifetimeStore.class);
        damageStore.createAndSet(eid, duration);
    }
}
