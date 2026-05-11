package io.bdc.painttd.world.act;

import io.bdc.painttd.world.*;

/**
 * Act是静态函数型数据, 计划用于实体的自定义行为, 策略模式行为等.
 * 不可持有实体级状态
 * 建议按家族实现
 */
@Deprecated
public interface Act {
    /** 生成一个运行时实例 */
    Act runtimeInstance();

    void act(int eid);

    /** 运行时实例可用, 缓存store和api依赖 */
    void onBind(WorldAccess binder);
}
