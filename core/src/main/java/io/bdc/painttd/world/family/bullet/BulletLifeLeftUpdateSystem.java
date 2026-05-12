package io.bdc.painttd.world.family.bullet;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.system.*;

public class BulletLifeLeftUpdateSystem extends WorldSystem {
    BulletLifetimeStore store;
    BulletLifetimeAPI api;

    public BulletLifeLeftUpdateSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        store = binder.getStore(BulletLifetimeStore.class);
        api = binder.getApi(BulletLifetimeAPI.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < store.size(); slot++) {
            float lifeLeft = store.lifeLeftArray.get(slot);
            if (lifeLeft > 0) {
                store.lifeLeftArray.set(slot, lifeLeft - 1);
            } else {
                api.recycle(store.eidOf(slot));
            }
        }
    }
}
