package io.bdc.painttd.world.system.render;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import io.bdc.painttd.infra.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class DrawHitboxSystem extends WorldSystem {
    TransformStore transformStore;
    HitboxStore hitboxStore;
    EntityHealthStore hpStore;
    CollisionBodyStore collisionStore;

    private static Rectangle rect = new Rectangle();
    private static Vector2 pos = new Vector2();

    public DrawHitboxSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        transformStore =  binder.getStore(TransformStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
        hpStore = binder.getStore(EntityHealthStore.class);
        collisionStore = binder.getStore(CollisionBodyStore.class);
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

            if (hpStore.has(eid)) {
                int slot = hpStore.slotOf(eid);
                float hp = hpStore.healths.get(slot);
                float maxHp = hpStore.maxHealths.get(slot);
                hbSize = hbSize * (hp / maxHp * 0.5f + 0.5f);
            }

            rect.setSize(hbSize).setCenter(pos.x, pos.y);

            var color = Color.WHITE;
            if (collisionStore.isStatic(eid)) color = Color.MAROON;

            RenderHub.batch.setColor(color);
            RenderHub.line.setStroke(1f);
            RenderHub.line.rect(rect);
        }
    }
}
