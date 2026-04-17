package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.store.request.*;

public final class CollisionBodyStep implements AssembleStep {
    public static final String TYPE = "CollisionBodyStep";

    public int bodyType = CollisionBodyStore.BODY_DYNAMIC;

    public CollisionBodyStep setup(int bodyType) {
        this.bodyType = bodyType;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var store = binder.getStore(CollisionBodyStore.class);
        if (store == null) {
            throw new IllegalStateException("Target store is missed.");
        }
        store.createAndSet(eid, bodyType);
    }
}
