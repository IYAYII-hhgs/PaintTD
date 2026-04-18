package io.bdc.painttd.world.assemble.step.post;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.request.*;

import static io.bdc.painttd.PaintTD.app;

public final class PostTransformStep implements PostSpawnStep, Pool.Poolable {
    public float x, y;

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var transformAPI = binder.getApi(TransformAPI.class);
        if (transformAPI == null) {
            throw new IllegalStateException("Target api is missed.");
        }

        boolean set = transformAPI.setTransform(eid, x, y);
        if (!set) {
            app.log.error("TransformStore not created for entity: " + eid);
        }
    }

    @Override
    public void reset() {
        x = y = 0;
    }
}
