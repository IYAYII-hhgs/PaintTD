package io.bdc.painttd.world.family.bullet;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class BulletValidationSystem extends WorldSystem {
    BulletStore bulletStore;
    DestroyQueue destroyQueue;

    public BulletValidationSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        bulletStore = binder.getStore(BulletStore.class);
        destroyQueue = binder.getStore(DestroyQueue.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < bulletStore.size(); slot++) {
            if (bulletStore.targetEntityArray.get(slot) == -1) {
                destroyQueue.add(bulletStore.eidOf(slot));
            }
        }
    }
}
