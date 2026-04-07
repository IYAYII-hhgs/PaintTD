package io.bdc.painttd.world.assemble.step;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.request.*;

/**
 * 实体装配模块的基类.
 */
public interface EntitySpawnStep {
    /** 在目标Stores中注册实体eid. 使用binder获取Store. */
    void run(int eid, EntitySpawnRequest req, WorldAccess binder);
}
