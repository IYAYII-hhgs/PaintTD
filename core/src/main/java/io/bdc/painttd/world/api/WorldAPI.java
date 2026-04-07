package io.bdc.painttd.world.api;

import io.bdc.painttd.world.*;

/**
 * 封装特定能力域的计算和立即读写方法, 供其他API和WorldSystem调用
 */

public interface WorldAPI {
    /** 缓存store依赖 */
    default void onBind(WorldAccess binder) {
    }
}
