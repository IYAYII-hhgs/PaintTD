package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class WorldBoundsBounceSystem extends WorldSystem {
    public CollisionBodyStore collisionBodyStore;
    public TransformStore transformStore;
    public HitboxStore hitboxStore;
    public VelocityStore velocityStore;
    public MapStore mapStore;

    public float bounceFactor = 0.6f;

    public WorldBoundsBounceSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        collisionBodyStore = binder.getStore(CollisionBodyStore.class);
        transformStore = binder.getStore(TransformStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
        velocityStore = binder.getStore(VelocityStore.class);
        mapStore = binder.getStore(MapStore.class);
    }

    @Override
    public void run(float delta) {
        float worldWidth = mapStore.width;
        float worldHeight = mapStore.height;

        for (int slot = 0; slot < collisionBodyStore.size(); slot++) {
            int eid = collisionBodyStore.eidOf(slot);
            if (!collisionBodyStore.isDynamic(eid)) {
                continue;
            }
            if (!transformStore.has(eid) || !hitboxStore.has(eid)) {
                continue;
            }

            float half = hitboxStore.get(eid) * 0.5f;
            float x = transformStore.getX(eid);
            float y = transformStore.getY(eid);
            float vx = velocityStore.getX(eid);
            float vy = velocityStore.getY(eid);

            boolean transformDirty = false;
            boolean velocityDirty = false;

            float minX = half;
            float maxX = worldWidth - half;
            if (minX > maxX) {
                x = worldWidth * 0.5f;
                vx = 0f;
                transformDirty = true;
                velocityDirty = true;
            } else {
                if (x < minX) {
                    x = minX;
                    if (vx < 0f) {
                        vx = -vx * bounceFactor;
                    }
                    transformDirty = true;
                    velocityDirty = true;
                } else if (x > maxX) {
                    x = maxX;
                    if (vx > 0f) {
                        vx = -vx * bounceFactor;
                    }
                    transformDirty = true;
                    velocityDirty = true;
                }
            }

            float minY = half;
            float maxY = worldHeight - half;
            if (minY > maxY) {
                y = worldHeight * 0.5f;
                vy = 0f;
                transformDirty = true;
                velocityDirty = true;
            } else {
                if (y < minY) {
                    y = minY;
                    if (vy < 0f) {
                        vy = -vy * bounceFactor;
                    }
                    transformDirty = true;
                    velocityDirty = true;
                } else if (y > maxY) {
                    y = maxY;
                    if (vy > 0f) {
                        vy = -vy * bounceFactor;
                    }
                    transformDirty = true;
                    velocityDirty = true;
                }
            }

            if (transformDirty) {
                transformStore.set(eid, x, y);
            }
            if (velocityDirty && velocityStore.has(eid)) {
                velocityStore.set(eid, vx, vy);
            }
        }
    }
}
