package io.bdc.painttd.world.assemble.step.post;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.store.request.*;

import static io.bdc.painttd.PaintTD.app;

public final class PostTestRandomVelocityStep implements PostSpawnStep, Pool.Poolable {
    public float x, y;

    public PostTestRandomVelocityStep set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var store = binder.getStore(VelocityStore.class);
        if (store == null) {
            throw new IllegalStateException("Target store is missed.");
        }

        boolean set = store.set(eid, MathUtils.random(-x, x), MathUtils.random(-y, y));
        if (!set) {
            app.log.error("VelocityStore not created for entity: " + eid);
        }
    }

    @Override
    public void reset() {
        x = y = 0;
    }
}
