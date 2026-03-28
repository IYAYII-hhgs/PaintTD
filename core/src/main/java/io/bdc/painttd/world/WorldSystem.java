package io.bdc.painttd.world;

public abstract class WorldSystem {
    public final WorldRuntime world;
    public final WorldPhase phase;
    public final int order;

    protected WorldSystem(WorldRuntime world, WorldPhase phase, int order) {
        this.world = world;
        this.phase = phase;
        this.order = order;
    }

    public abstract void run(float delta);
}
