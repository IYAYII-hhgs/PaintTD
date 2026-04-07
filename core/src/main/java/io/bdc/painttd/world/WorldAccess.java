package io.bdc.painttd.world;

import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;

public class WorldAccess {
    public final WorldRuntime world;

    public WorldAccess(WorldRuntime world) {
        this.world = world;
    }

    public <T extends WorldStore> T getStore(Class<T> type) {
        return world.getStore(type);
    }

    public <T extends WorldStore> boolean hasStore(Class<T> type) {
        return world.hasStore(type);
    }

    public <T extends WorldStore> T optionalStore(Class<T> type) {
        return world.hasStore(type) ? world.getStore(type) : null;
    }

    public <T extends WorldAPI> T getApi(Class<T> type) {
        return world.getApi(type);
    }

    public <T extends WorldAPI> boolean hasApi(Class<T> type) {
        return world.hasApi(type);
    }

    public <T extends WorldAPI> T optionalApi(Class<T> type) {
        return world.hasApi(type) ? world.getApi(type) : null;
    }
}