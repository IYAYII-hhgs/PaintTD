package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class WorldBoundsBounceSystem extends WorldSystem {
    public CollisionBodyStore collisionBodyStore;
    public PositionStore positionStore;
    public TransformAPI transformAPI;
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
        positionStore = binder.getStore(PositionStore.class);
        transformAPI = binder.getApi(TransformAPI.class);
        hitboxStore = binder.getStore(HitboxStore.class);
        velocityStore = binder.getStore(VelocityStore.class);
        mapStore = binder.getStore(MapStore.class);
    }

    @Override
    public void run(float delta) {
        float worldWidth = mapStore.width;
        float worldHeight = mapStore.height;
        int[] bodyTypeItems = collisionBodyStore.bodyTypeArray.items;
        float[] transformXItems = positionStore.x.items;
        float[] transformYItems = positionStore.y.items;
        float[] hitboxItems = hitboxStore.hb.items;
        float[] velocityXItems = velocityStore.x.items;
        float[] velocityYItems = velocityStore.y.items;

        for (int bodySlot = 0; bodySlot < collisionBodyStore.size(); bodySlot++) {
            if (bodyTypeItems[bodySlot] != CollisionBodyStore.BODY_DYNAMIC) {
                continue;
            }

            int eid = collisionBodyStore.eidOf(bodySlot);
            int transformSlot = positionStore.slotOf(eid);
            int hitboxSlot = hitboxStore.slotOf(eid);
            if (transformSlot < 0 || hitboxSlot < 0) {
                continue;
            }

            int velocitySlot = velocityStore.slotOf(eid);

            float half = hitboxItems[hitboxSlot] * 0.5f;
            float x = transformXItems[transformSlot];
            float y = transformYItems[transformSlot];
            float vx = velocitySlot >= 0 ? velocityXItems[velocitySlot] : 0f;
            float vy = velocitySlot >= 0 ? velocityYItems[velocitySlot] : 0f;

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
                transformXItems[transformSlot] = x;
                transformYItems[transformSlot] = y;
                transformAPI.markDirty(eid);
            }
            if (velocityDirty && velocitySlot >= 0) {
                velocityXItems[velocitySlot] = vx;
                velocityYItems[velocitySlot] = vy;
            }
        }
    }
}
