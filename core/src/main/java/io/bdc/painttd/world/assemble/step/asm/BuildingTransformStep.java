package io.bdc.painttd.world.assemble.step.asm;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.store.request.*;

/**
 * 具有该Step的EntityDef能够将{@code EntitySpawnRequest}的坐标字段取整并自动注册为新实体的实际坐标
 */

public final class BuildingTransformStep implements AssembleStep {
    public static final String TYPE = "TransformStep";

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var transformStore = binder.getStore(TransformStore.class);
        if (transformStore == null) {
            throw new IllegalStateException("Target store is missed.");
        }
        transformStore.createAndSet(eid, MathUtils.floor(req.x) + 0.5f, MathUtils.floor(req.y) + 0.5f);
    }
}
