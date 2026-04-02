package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;

/**
 * 具有该Step的EntityDef能够将{@code EntitySpawnRequest}的坐标字段注册为新实体的实际坐标
 */

public final class TransformStep implements AssembleStep {
    public static final String TYPE = "TransformStep";

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldStoreBinder binder) {
        var transformStore = binder.require(TransformStore.class);
        if (transformStore == null) {
            throw new IllegalStateException("Target store is missed.");
        }
        transformStore.put(eid, req.x, req.y);
    }
}
