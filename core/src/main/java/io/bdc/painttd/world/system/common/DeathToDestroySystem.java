package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class DeathToDestroySystem extends WorldSystem {
    EntityHealthStore healthStore;
    DestroyQueue queue;

    public DeathToDestroySystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        super.onBind(binder);
        healthStore = binder.getStore(EntityHealthStore.class);
        queue = binder.getStore(DestroyQueue.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < healthStore.size(); slot++) {
            float hp = healthStore.healths.get(slot);
            if (hp <= 0) {
                queue.add(healthStore.eidOf(slot));
            }
        }
    }
}
