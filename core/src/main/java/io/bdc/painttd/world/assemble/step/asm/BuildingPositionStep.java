package io.bdc.painttd.world.assemble.step.asm;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.request.*;

/**
 * 具有该Step的EntityDef能够将{@code EntitySpawnRequest}的坐标字段取整并自动注册为新实体的实际坐标
 */

public final class BuildingPositionStep implements AssembleStep {
    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var transformAPI = binder.getApi(TransformAPI.class);
        if (transformAPI == null) {
            throw new IllegalStateException("Target api is missed.");
        }
        transformAPI.createAndSetPosition(eid, MathUtils.floor(req.x) + 0.5f, MathUtils.floor(req.y) + 0.5f);
    }
}
