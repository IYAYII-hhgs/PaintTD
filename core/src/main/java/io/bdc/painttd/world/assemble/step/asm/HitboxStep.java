package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.request.*;

public final class HitboxStep implements AssembleStep {
    public float size = 1;

    public HitboxStep setup(float size) {
        this.size = size;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var api = binder.getApi(TransformAPI.class);
        api.createAndSetHitbox(eid, size);
    }
}
