package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class CollisionSoftRepulsionSystem extends WorldSystem {
    public CollisionBodyStore collisionBodyStore;
    public TransformStore transformStore;
    public HitboxStore hitboxStore;
    public VelocityStore velocityStore;

    public float pushPerOverlap = 0.02f;

    public CollisionSoftRepulsionSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        collisionBodyStore = binder.getStore(CollisionBodyStore.class);
        transformStore = binder.getStore(TransformStore.class);
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

        int transformSlotA = transformStore.slotOf(eidA);
        int transformSlotB = transformStore.slotOf(eidB);
        if (transformSlotA < 0 || transformSlotB < 0) {
            return;
        }

        int hitboxSlotA = hitboxStore.slotOf(eidA);
        int hitboxSlotB = hitboxStore.slotOf(eidB);
        if (hitboxSlotA < 0 || hitboxSlotB < 0) {
            return;
        }

        float ax = transformStore.x.items[transformSlotA];
        float ay = transformStore.y.items[transformSlotA];
        float bx = transformStore.x.items[transformSlotB];
        float by = transformStore.y.items[transformSlotB];

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

        float sign = resolveAxisSign(bx - ax, eidA, eidB);
        float push = overlapX * pushPerOverlap;
        addVelocity(velocitySlotA, velocityXItems, velocityYItems, -sign * push, 0f);
        addVelocity(velocitySlotB, velocityXItems, velocityYItems, sign * push, 0f);

        sign = resolveAxisSign(by - ay, eidA, eidB);
        push = (overlapY) * pushPerOverlap;
        addVelocity(velocitySlotA, velocityXItems, velocityYItems, 0f, -sign * push);
        addVelocity(velocitySlotB, velocityXItems, velocityYItems, 0f, sign * push);
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

    private void addVelocity(int velocitySlot, float[] velocityXItems, float[] velocityYItems, float addX, float addY) {
        if (velocitySlot < 0) {
            return;
        }
        velocityXItems[velocitySlot] += addX;
        velocityYItems[velocitySlot] += addY;
    }
}
