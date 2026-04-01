package io.bdc.painttd.world.act;

import io.bdc.painttd.world.*;

public abstract class ActType {
    public abstract int id();

    public String stringId() {
        return Integer.toString(id());
    }

    /** 缓存store依赖 */
    public abstract void onStoreBind(WorldStoreBinder binder);
}
