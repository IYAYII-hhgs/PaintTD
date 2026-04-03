package io.bdc.painttd.world.system.render;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import io.bdc.painttd.infra.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class DrawHitboxSystem extends WorldSystem {
    public TransformStore transformStore;
    public HitboxStore hitboxStore;

    private static Rectangle rect = new Rectangle();
    private static Vector2 pos = new Vector2();

    public DrawHitboxSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onStoreBind(WorldStoreBinder binder) {
        transformStore =  binder.require(TransformStore.class);
        hitboxStore = binder.require(HitboxStore.class);
    }

    @Override
    public void run(float delta) {
        for (int i = 0; i < transformStore.size(); i++) {
            int eid = transformStore.eidOf(i);
            transformStore.get(eid, pos);
            pos.scl(RenderHub.scl);

            float hbSize = 1;
            if (hitboxStore.has(eid)) {
                hbSize = hitboxStore.get(eid);
            }
            hbSize *= RenderHub.scl;

            rect.setSize(hbSize).setCenter(pos.x, pos.y);

            RenderHub.batch.setColor(Color.WHITE);
            RenderHub.line.setStroke(1f);
            RenderHub.line.rect(rect);

        }
    }
}
