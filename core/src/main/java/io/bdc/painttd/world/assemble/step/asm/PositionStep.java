package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.request.*;

/**
 * 具有该Step的EntityDef能够将{@code EntitySpawnRequest}的坐标字段注册为新实体的实际坐标
 */

public final class PositionStep implements AssembleStep {
    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var transformAPI = binder.getApi(TransformAPI.class);
        if (transformAPI == null) {
            throw new IllegalStateException("Target api is missed.");
        }
        transformAPI.createAndSetPosition(eid, req.x, req.y);
    }
}
