package io.bdc.painttd.world.system.render;

import io.bdc.painttd.infra.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.system.*;

public class PreRenderSystem extends WorldSystem {
    public PreRenderSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onStoreBind(WorldStoreBinder binder) {
        super.onStoreBind(binder);
    }

    @Override
    public void run(float delta) {
        world.worldView.viewport.apply();
        RenderHub.batch.setProjectionMatrix(world.worldView.camera.combined);
        RenderHub.batch.begin();
    }
}
