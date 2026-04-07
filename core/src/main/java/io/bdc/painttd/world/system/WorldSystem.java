package io.bdc.painttd.world.system;

import io.bdc.painttd.world.*;

/**
 * World级单例. 允许持有World级状态, 不允许持有实体级状态.
 */
public abstract class WorldSystem {
    public final WorldRuntime world;
    public final WorldPhase phase;
    public final int order;

    protected WorldSystem(WorldRuntime world, WorldPhase phase, int order) {
        this.world = world;
        this.phase = phase;
        this.order = order;
    }

    /** 缓存store依赖 */
    public void onBind(WorldAccess binder) {
    }

    public abstract void run(float delta);
}
