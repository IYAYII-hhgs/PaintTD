package io.bdc.painttd.world.api;

import io.bdc.painttd.world.*;

public class WorldTimeAPI implements WorldAPI {
    WorldRuntime world;

    public int getTick() {
        return world.tick;
    }

    public int sinceTick(int tick) {
        return world.tick - tick;
    }

    public float getTime() {
        return world.time;
    }

    public float sinceTime(float time) {
        return world.time - time;
    }

    @Override
    public void onBind(WorldAccess binder) {
        world = binder.world;
    }
}
