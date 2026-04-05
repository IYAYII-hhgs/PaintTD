package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.request.*;
import io.bdc.painttd.world.store.*;

public final class HitboxStep implements AssembleStep {
    public static final String TYPE = "HitboxStep";

    public float size = 1;

    public HitboxStep setup(float size) {
        this.size = size;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldStoreBinder binder) {
        var store = binder.require(HitboxStore.class);
        if (store == null) {
            throw new IllegalStateException("Target store is missed.");
        }
        store.put(eid, size);
    }
}
