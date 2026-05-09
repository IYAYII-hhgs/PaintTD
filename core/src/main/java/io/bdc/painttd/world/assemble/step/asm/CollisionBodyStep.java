package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.store.request.*;

import static io.bdc.painttd.world.store.CollisionBodyStore.*;

public final class CollisionBodyStep implements AssembleStep {
    public int bodyType = BODY_DYNAMIC;
    public int category = 0;
    public int mask = 0;

    public CollisionBodyStep bodyType(int bodyType) {
        this.bodyType = bodyType;
        return this;
    }

    public CollisionBodyStep category(int category) {
        this.category = category;
        return this;
    }

    public CollisionBodyStep mask(int mask) {
        this.mask = mask;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var store = binder.getStore(CollisionBodyStore.class);
        if (store == null) {
            throw new IllegalStateException("Target store is missed.");
        }
        store.createAndSet(eid, bodyType, category, mask);
    }
}
