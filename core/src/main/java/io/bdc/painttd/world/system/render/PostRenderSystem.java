package io.bdc.painttd.world.system.render;

import io.bdc.painttd.infra.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.system.*;

public class PostRenderSystem extends WorldSystem {
    public PostRenderSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onStoreBind(WorldStoreBinder binder) {
        super.onStoreBind(binder);
    }

    @Override
    public void run(float delta) {
        RenderHub.batch.end();
    }
}
