package io.bdc.painttd.world.system.common;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

/**
 * 静态阻挡响应的应用层 system。
 * 以Handler形式同时消费两类接触：EC 的墙格接触，以及 EE 里的 dynamic-static 接触，
 * 再统一转换成“动态实体撞到静态阻挡”的响应。
 * system 执行时, 再实际应用缓存的响应.
 * <p>
 * 该系统当前三层式设计想表达的重点：
 * 检测层继续区分 EE 和 EC，
 * 分发层继续按 family 提供稳定入口，
 * 但应用层可以按玩法语义把不同 family 的接触合并处理。
 * 所以这个 system 能同时处理墙格和静态实体是因为它们在应用层都属于"静态阻挡反弹"。
 */
public class CollisionSolidBounceSystem extends WorldSystem {
    public CollisionBodyStore collisionBodyStore;
    public CollisionSolidBounceStore solidBounceStore;
    public TransformStore transformStore;
    public HitboxStore hitboxStore;
    public VelocityStore velocityStore;
    public MapStore mapStore;

    public float bounceFactor = 0.6f;

    public CollisionSolidBounceSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        collisionBodyStore = binder.getStore(CollisionBodyStore.class);
        solidBounceStore = binder.getStore(CollisionSolidBounceStore.class);
        transformStore = binder.getStore(TransformStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
        velocityStore = binder.getStore(VelocityStore.class);
        mapStore = binder.getStore(MapStore.class);

        var collisionDispatch = binder.getApi(CollisionDispatchAPI.class);
        collisionDispatch.onEc(this::handleEc);
        collisionDispatch.onUndirectedEe(this::handleUndirectedEe);
    }

    @Override
    public void run(float delta) {
        int[] maskItems = solidBounceStore.masks.items;
        float[] velocityXItems = velocityStore.x.items;
        float[] velocityYItems = velocityStore.y.items;

        for (int solidSlot = 0; solidSlot < solidBounceStore.size(); solidSlot++) {
            int eid = solidBounceStore.eidOf(solidSlot);
            int velocitySlot = velocityStore.slotOf(eid);
            if (velocitySlot < 0) {
                continue;
            }

            int mask = maskItems[solidSlot];
            float vx = velocityXItems[velocitySlot];
            float vy = velocityYItems[velocitySlot];

            if ((mask & (CollisionSolidBounceStore.HIT_LEFT | CollisionSolidBounceStore.HIT_RIGHT))
                    == (CollisionSolidBounceStore.HIT_LEFT | CollisionSolidBounceStore.HIT_RIGHT)) {
                vx = 0f;
            } else {
                if ((mask & CollisionSolidBounceStore.HIT_LEFT) != 0 && vx < 0f) {
                    vx = -vx * bounceFactor;
                }
                if ((mask & CollisionSolidBounceStore.HIT_RIGHT) != 0 && vx > 0f) {
                    vx = -vx * bounceFactor;
                }
            }

            if ((mask & (CollisionSolidBounceStore.HIT_DOWN | CollisionSolidBounceStore.HIT_UP))
                    == (CollisionSolidBounceStore.HIT_DOWN | CollisionSolidBounceStore.HIT_UP)) {
                vy = 0f;
            } else {
                if ((mask & CollisionSolidBounceStore.HIT_DOWN) != 0 && vy < 0f) {
                    vy = -vy * bounceFactor;
                }
                if ((mask & CollisionSolidBounceStore.HIT_UP) != 0 && vy > 0f) {
                    vy = -vy * bounceFactor;
                }
            }

            velocityXItems[velocitySlot] = vx;
            velocityYItems[velocitySlot] = vy;
        }

        solidBounceStore.clear();
    }

    private void handleEc(int eid, int cellIndex) {
        int transformSlot = transformStore.slotOf(eid);
        int hitboxSlot = hitboxStore.slotOf(eid);
        if (transformSlot < 0 || hitboxSlot < 0) {
            return;
        }

        int cellX = cellIndex % mapStore.width;
        int cellY = cellIndex / mapStore.width;
        float[] transformXItems = transformStore.x.items;
        float[] transformYItems = transformStore.y.items;
        float[] hitboxItems = hitboxStore.hb.items;
        float entityX = transformXItems[transformSlot];
        float entityY = transformYItems[transformSlot];
        float half = hitboxItems[hitboxSlot] * 0.5f;
        float dx = entityX - (cellX + 0.5f);
        float dy = entityY - (cellY + 0.5f);
        float overlapX = half + 0.5f - Math.abs(dx);
        float overlapY = half + 0.5f - Math.abs(dy);
        if (overlapX <= 0f || overlapY <= 0f) {
            return;
        }

        int mask = 0;
        float diff = Math.abs(overlapX - overlapY);
        if (overlapX < overlapY || diff <= MathUtils.FLOAT_ROUNDING_ERROR) {
            mask |= dx <= 0f ? CollisionSolidBounceStore.HIT_RIGHT : CollisionSolidBounceStore.HIT_LEFT;
        }
        if (overlapY < overlapX || diff <= MathUtils.FLOAT_ROUNDING_ERROR) {
            mask |= dy <= 0f ? CollisionSolidBounceStore.HIT_UP : CollisionSolidBounceStore.HIT_DOWN;
        }

        solidBounceStore.addMask(eid, mask);
    }

    private void handleUndirectedEe(int eidA, int eidB) {
        int bodySlotA = collisionBodyStore.slotOf(eidA);
        int bodySlotB = collisionBodyStore.slotOf(eidB);
        if (bodySlotA < 0 || bodySlotB < 0) {
            return;
        }

        int[] bodyTypeItems = collisionBodyStore.bodyTypes.items;
        boolean aDynamic = bodyTypeItems[bodySlotA] == CollisionBodyStore.BODY_DYNAMIC;
        boolean bDynamic = bodyTypeItems[bodySlotB] == CollisionBodyStore.BODY_DYNAMIC;
        boolean aStatic = bodyTypeItems[bodySlotA] == CollisionBodyStore.BODY_STATIC;
        boolean bStatic = bodyTypeItems[bodySlotB] == CollisionBodyStore.BODY_STATIC;
        if (aDynamic == bDynamic || aStatic == bStatic) {
            return;
        }

        int dynamicEid = aDynamic ? eidA : eidB;
        int staticEid = aStatic ? eidA : eidB;
        int dynamicTransformSlot = transformStore.slotOf(dynamicEid);
        int staticTransformSlot = transformStore.slotOf(staticEid);
        if (dynamicTransformSlot < 0 || staticTransformSlot < 0) {
            return;
        }

        int dynamicHitboxSlot = hitboxStore.slotOf(dynamicEid);
        int staticHitboxSlot = hitboxStore.slotOf(staticEid);
        if (dynamicHitboxSlot < 0 || staticHitboxSlot < 0) {
            return;
        }

        float[] transformXItems = transformStore.x.items;
        float[] transformYItems = transformStore.y.items;
        float[] hitboxItems = hitboxStore.hb.items;
        float dynamicX = transformXItems[dynamicTransformSlot];
        float dynamicY = transformYItems[dynamicTransformSlot];
        float staticX = transformXItems[staticTransformSlot];
        float staticY = transformYItems[staticTransformSlot];
        float dynamicHalf = hitboxItems[dynamicHitboxSlot] * 0.5f;
        float staticHalf = hitboxItems[staticHitboxSlot] * 0.5f;
        float dx = dynamicX - staticX;
        float dy = dynamicY - staticY;
        float overlapX = dynamicHalf + staticHalf - Math.abs(dx);
        float overlapY = dynamicHalf + staticHalf - Math.abs(dy);
        if (overlapX <= 0f || overlapY <= 0f) {
            return;
        }

        int mask = 0;
        float diff = Math.abs(overlapX - overlapY);
        if (overlapX < overlapY || diff <= MathUtils.FLOAT_ROUNDING_ERROR) {
            mask |= dx <= 0f ? CollisionSolidBounceStore.HIT_RIGHT : CollisionSolidBounceStore.HIT_LEFT;
        }
        if (overlapY < overlapX || diff <= MathUtils.FLOAT_ROUNDING_ERROR) {
            mask |= dy <= 0f ? CollisionSolidBounceStore.HIT_UP : CollisionSolidBounceStore.HIT_DOWN;
        }

        solidBounceStore.addMask(dynamicEid, mask);
    }
}
