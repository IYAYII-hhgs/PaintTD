package io.bdc.painttd.world;

import io.bdc.painttd.world.store.*;

public class WorldStoreBinder {
    public final WorldRuntime world;

    public WorldStoreBinder(WorldRuntime world) {
        this.world = world;
    }

    public <T extends WorldStore> T require(Class<T> type) {
        return world.getStore(type);
    }

    public <T extends WorldStore> T optional(Class<T> type) {
        return world.hasStore(type) ? world.getStore(type) : null;
    }
}
