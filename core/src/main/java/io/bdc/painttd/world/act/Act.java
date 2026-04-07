package io.bdc.painttd.world.act;

import io.bdc.painttd.world.*;

/**
 * Act是ActStore中存储的函数型数据, 被ActSystem执行, 计划用于实体的自定义行为, 策略模式行为等.
 * 不可持有实体级状态
 * 建议XAct, XActSystem, XActStore按家族实现
 */
public interface Act {
    String id();

    /** 缓存store和api依赖 */
    void onBind(WorldAccess binder);
}
