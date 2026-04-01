package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class EntityRecycleSystem extends WorldSystem {
    public DestroyRequestQueue destroyQueue;
    public EntityAssembler entityAssembler;

    public EntityRecycleSystem(WorldRuntime world, WorldPhase phase, int order, EntityAssembler entityAssembler) {
        super(world, phase, order);
        this.entityAssembler = entityAssembler;
    }

    @Override
    public void onStoreBind(WorldStoreBinder binder) {
        destroyQueue = binder.require(DestroyRequestQueue.class);
    }

    @Override
    public void run(float delta) {
        for (int i = 0; i < destroyQueue.size(); i++) {
            entityAssembler.destroy(world, destroyQueue.eids.get(i));
        }
        destroyQueue.clear();
    }
}
