package io.bdc.painttd.world.system.common;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

/**
 * 静态阻挡响应的应用层 system。
 * 它同时消费来自 EC 与 EE 的静态阻挡接触，
 * 再统一转换成“动态实体撞到静态阻挡”的响应。
 * <p>
 * 这正是当前三层式设计的一部分：检测层继续区分接触来源，
 * 分发层提供稳定入口，应用层则按效果语义把不同 family 的接触合并处理。
 * 当前中间态除了 side mask，还会累计四方向推出量；system 执行时先解穿透，再修正速度。
 */
public class CollisionSolidBounceSystem extends WorldSystem {
    public CollisionBodyStore collisionBodyStore;
    public CollisionSolidBounceStore solidBounceStore;
    public TransformStore transformStore;
    public EntityTeamStore teamStore;
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
        teamStore = binder.getStore(EntityTeamStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
        velocityStore = binder.getStore(VelocityStore.class);
        mapStore = binder.getStore(MapStore.class);

        var collisionDispatch = binder.getApi(CollisionDispatchAPI.class);
        collisionDispatch.onEcWall(this::handleEc);
        collisionDispatch.onEcStain(this::handleEcStain);
        collisionDispatch.onUndirectedEe(this::handleUndirectedEe);
    }

    @Override
    public void run(float delta) {
        int[] maskItems = solidBounceStore.masks.items;
        float[] pushLeftItems = solidBounceStore.pushLeft.items;
        float[] pushRightItems = solidBounceStore.pushRight.items;
        float[] pushDownItems = solidBounceStore.pushDown.items;
        float[] pushUpItems = solidBounceStore.pushUp.items;
        float[] transformXItems = transformStore.x.items;
        float[] transformYItems = transformStore.y.items;
        float[] velocityXItems = velocityStore.x.items;
        float[] velocityYItems = velocityStore.y.items;

        for (int solidSlot = 0; solidSlot < solidBounceStore.size(); solidSlot++) {
            int eid = solidBounceStore.eidOf(solidSlot);
            int transformSlot = transformStore.slotOf(eid);
            if (transformSlot >= 0) {
                transformXItems[transformSlot] += pushLeftItems[solidSlot] - pushRightItems[solidSlot];
                transformYItems[transformSlot] += pushDownItems[solidSlot] - pushUpItems[solidSlot];
            }

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

    private void handleEcStain(int eid, int cellIndex) {
        if (mapStore.cells[cellIndex] == 1) return;// handled by wall handler
        int eTeam = teamStore.get(eid);
        int cellTeam = mapStore.teamMask[cellIndex];
        if (eTeam == cellTeam) return;
        handleEc(eid, cellIndex);
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

        addResponse(eid, mask, overlapX, overlapY);
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

        addResponse(dynamicEid, mask, overlapX, overlapY);
    }

    private void addResponse(int eid, int mask, float overlapX, float overlapY) {
        float pushLeft = (mask & CollisionSolidBounceStore.HIT_LEFT) != 0 ? overlapX : 0f;
        float pushRight = (mask & CollisionSolidBounceStore.HIT_RIGHT) != 0 ? overlapX : 0f;
        float pushDown = (mask & CollisionSolidBounceStore.HIT_DOWN) != 0 ? overlapY : 0f;
        float pushUp = (mask & CollisionSolidBounceStore.HIT_UP) != 0 ? overlapY : 0f;
        solidBounceStore.addResponse(eid, mask, pushLeft, pushRight, pushDown, pushUp);
    }
}
