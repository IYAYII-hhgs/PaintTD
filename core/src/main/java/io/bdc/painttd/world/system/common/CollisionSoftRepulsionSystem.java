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

    public float pushPerOverlap = 0.1f;

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
        if (!collisionBodyStore.isDynamic(eidA) || !collisionBodyStore.isDynamic(eidB)) {
            return;
        }
        if (!transformStore.has(eidA) || !transformStore.has(eidB)) {
            return;
        }
        if (!hitboxStore.has(eidA) || !hitboxStore.has(eidB)) {
            return;
        }

        float ax = transformStore.getX(eidA);
        float ay = transformStore.getY(eidA);
        float bx = transformStore.getX(eidB);
        float by = transformStore.getY(eidB);

        float ah = hitboxStore.get(eidA) * 0.5f;
        float bh = hitboxStore.get(eidB) * 0.5f;
        float overlapX = ah + bh - Math.abs(bx - ax);
        float overlapY = ah + bh - Math.abs(by - ay);
        if (overlapX <= 0f || overlapY <= 0f) {
            return;
        }

        float sign = resolveAxisSign(bx - ax, eidA, eidB);
        float push = overlapX * pushPerOverlap;
        addVelocity(eidA, -sign * push, 0f);
        addVelocity(eidB, sign * push, 0f);

        sign = resolveAxisSign(by - ay, eidA, eidB);
        push = overlapY * pushPerOverlap;
        addVelocity(eidA, 0f, -sign * push);
        addVelocity(eidB, 0f, sign * push);
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

    private void addVelocity(int eid, float addX, float addY) {
        float nextX = velocityStore.getX(eid) + addX;
        float nextY = velocityStore.getY(eid) + addY;
        velocityStore.set(eid, nextX, nextY);
    }
}
