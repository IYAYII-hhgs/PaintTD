package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class CollisionSoftRepulsionSystem extends WorldSystem {
    public CollisionBodyStore collisionBodyStore;
    public PositionStore positionStore;
    public HitboxStore hitboxStore;
    public VelocityStore velocityStore;

    public float pushPerOverlap = 0.04f, notMainAxisMultiplier = 0.1f;

    public CollisionSoftRepulsionSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        collisionBodyStore = binder.getStore(CollisionBodyStore.class);
        positionStore = binder.getStore(PositionStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
        velocityStore = binder.getStore(VelocityStore.class);

        binder.getApi(CollisionDispatchAPI.class).onUndirectedEe(this::handleUndirectedEe);
    }

    @Override
    public void run(float delta) {
    }

    private void handleUndirectedEe(int eidA, int eidB) {
        int bodySlotA = collisionBodyStore.slotOf(eidA);
        int bodySlotB = collisionBodyStore.slotOf(eidB);
        if (bodySlotA < 0 || bodySlotB < 0) {
            return;
        }

        int[] bodyTypeItems = collisionBodyStore.bodyTypes.items;
        if (bodyTypeItems[bodySlotA] != CollisionBodyStore.BODY_DYNAMIC
                || bodyTypeItems[bodySlotB] != CollisionBodyStore.BODY_DYNAMIC) {
            return;
        }

        int transformSlotA = positionStore.slotOf(eidA);
        int transformSlotB = positionStore.slotOf(eidB);
        if (transformSlotA < 0 || transformSlotB < 0) {
            return;
        }

        int hitboxSlotA = hitboxStore.slotOf(eidA);
        int hitboxSlotB = hitboxStore.slotOf(eidB);
        if (hitboxSlotA < 0 || hitboxSlotB < 0) {
            return;
        }

        float ax = positionStore.x.items[transformSlotA];
        float ay = positionStore.y.items[transformSlotA];
        float bx = positionStore.x.items[transformSlotB];
        float by = positionStore.y.items[transformSlotB];

        float ah = hitboxStore.hb.items[hitboxSlotA] * 0.5f;
        float bh = hitboxStore.hb.items[hitboxSlotB] * 0.5f;
        float overlapX = ah + bh - Math.abs(bx - ax);
        float overlapY = ah + bh - Math.abs(by - ay);
        if (overlapX <= 0f || overlapY <= 0f) {
            return;
        }

        float[] velocityXItems = velocityStore.x.items;
        float[] velocityYItems = velocityStore.y.items;

        int velocitySlotA = velocityStore.slotOf(eidA);
        int velocitySlotB = velocityStore.slotOf(eidB);
        if (velocitySlotA < 0 || velocitySlotB < 0) {
            return;
        }

        float sign = resolveAxisSign(bx - ax, eidA, eidB);
        float push = overlapX * pushPerOverlap;
        if (overlapX > overlapY) push *= notMainAxisMultiplier; // 排斥主轴检查
        velocityXItems[velocitySlotA] += -sign * push;
        velocityXItems[velocitySlotB] += sign * push;

        sign = resolveAxisSign(by - ay, eidA, eidB);
        push = overlapY * pushPerOverlap;
        if (overlapY > overlapX) push *= notMainAxisMultiplier; // 排斥主轴检查
        velocityYItems[velocitySlotA] += -sign * push;
        velocityYItems[velocitySlotB] += sign * push;
    }

    private float resolveAxisSign(float delta, int eidA, int eidB) {
        if (delta > 0f) {
            return 1f;
        }
        if (delta < 0f) {
            return -1f;
        }
        return eidA < eidB ? 1f : -1f;
    }
}
