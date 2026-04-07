package io.bdc.painttd.world.store.request;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.assemble.step.*;

public final class EntitySpawnRequest implements Pool.Poolable {
    public EntityDef entityDef;
    public float x;
    public float y;

    public final Array<PostSpawnStep> extraSteps = new Array<>();

    public EntitySpawnRequest() {}

    public EntitySpawnRequest setup(EntityDef entityDef, float x, float y) {
        this.entityDef = entityDef;
        this.x = x;
        this.y = y;
        return this;
    }

    public EntitySpawnRequest addStep(PostSpawnStep step) {
        extraSteps.add(step);
        return this;
    }

    @Override
    public void reset() {
        entityDef = null;
        x = 0f;
        y = 0f;
        extraSteps.clear();
    }
}
