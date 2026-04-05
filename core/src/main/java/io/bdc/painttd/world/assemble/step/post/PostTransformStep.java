package io.bdc.painttd.world.assemble.step.post;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.request.*;
import io.bdc.painttd.world.store.*;

public final class PostTransformStep implements PostSpawnStep, Pool.Poolable {
    public float x, y;

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldStoreBinder binder) {
        var transformStore = binder.require(TransformStore.class);
        if (transformStore == null) {
            throw new IllegalStateException("Target store is missed.");
        }

        transformStore.put(eid, x, y);
    }

    @Override
    public void reset() {
        x = y = 0;
    }
}
