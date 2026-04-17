package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.store.request.*;

public final class HitboxStep implements AssembleStep {
    public static final String TYPE = "HitboxStep";

    public float size = 1;

    public HitboxStep setup(float size) {
        this.size = size;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var store = binder.getStore(HitboxStore.class);
        if (store == null) {
            throw new IllegalStateException("Target store is missed.");
        }
        store.createAndSet(eid, size);
    }
}
