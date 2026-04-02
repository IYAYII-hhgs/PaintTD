package io.bdc.painttd.world.act;

import io.bdc.painttd.world.*;

/**
 * ActType是ActStore中存储的函数型数据, 计划用于实体自定义行为, 策略模式行为等.
 */
public abstract class ActType {
    public abstract int id();

    public String stringId() {
        return Integer.toString(id());
    }

    /** 缓存store依赖 */
    public abstract void onStoreBind(WorldStoreBinder binder);
}
