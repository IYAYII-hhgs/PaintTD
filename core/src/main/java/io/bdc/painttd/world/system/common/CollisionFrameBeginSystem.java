package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class CollisionFrameBeginSystem extends WorldSystem {
    public CollisionDispatchAPI collisionDispatchAPI;
    public CollisionDebugStore collisionDebugStore;

    public CollisionFrameBeginSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        collisionDispatchAPI = binder.getApi(CollisionDispatchAPI.class);
        collisionDebugStore = binder.getStore(CollisionDebugStore.class);
    }

    @Override
    public void run(float delta) {
        collisionDispatchAPI.beginFrame();
        collisionDebugStore.resetFrame();
    }
}
