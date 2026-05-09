package io.bdc.painttd.world.system.common;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

/**
 * EE 检测层的首版实现。
 * 先做 bucket 粗筛，再做 AABB 精筛；命中后不缓存结果，而是直接发给分发层。
 * <p>
 * 只产出两个实体的接触事实，不处理伤害、反弹或其他应用效果。
 */
public class DetectEeAabbSystem extends WorldSystem {
    public CollisionBodyStore collisionBodyStore;
    public PositionStore positionStore;
    public HitboxStore hitboxStore;
    public TileBucketQueryAPI tileBucketQueryAPI;
    public CollisionDispatchAPI collisionDispatchAPI;
    public CollisionDebugStore collisionDebugStore;

    public DetectEeAabbSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        collisionBodyStore = binder.getStore(CollisionBodyStore.class);
        positionStore = binder.getStore(PositionStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
        tileBucketQueryAPI = binder.getApi(TileBucketQueryAPI.class);
        collisionDispatchAPI = binder.getApi(CollisionDispatchAPI.class);
        collisionDebugStore = binder.getStore(CollisionDebugStore.class);
    }

    @Override
    public void run(float delta) {
        int[] bodyTypeItems = collisionBodyStore.bodyTypeArray.items;
        float[] transformXItems = positionStore.x.items;
        float[] transformYItems = positionStore.y.items;
        float[] hitboxItems = hitboxStore.hb.items;

        int eeQueryCount = collisionDebugStore.eeQueryCount;
        int eeCandidateCount = collisionDebugStore.eeCandidateCount;
        int eeOverlapCount = collisionDebugStore.eeOverlapCount;

        for (int sourceBodySlot = 0; sourceBodySlot < collisionBodyStore.size(); sourceBodySlot++) {
            // 静态实体 排除
            if (bodyTypeItems[sourceBodySlot] != CollisionBodyStore.BODY_DYNAMIC) {
                continue;
            }

            int sourceEid = collisionBodyStore.eidOf(sourceBodySlot);
            int sourceTransformSlot = positionStore.slotOf(sourceEid);
            int sourceHitboxSlot = hitboxStore.slotOf(sourceEid);
            if (sourceTransformSlot < 0 || sourceHitboxSlot < 0) {
                continue;
            }

            float sourceSize = hitboxItems[sourceHitboxSlot];
            if (sourceSize <= 0f) {
                continue;
            }

            float sourceX = transformXItems[sourceTransformSlot];
            float sourceY = transformYItems[sourceTransformSlot];
            float sourceHalf = sourceSize * 0.5f;
            float sourceMinX = sourceX - sourceHalf;
            float sourceMinY = sourceY - sourceHalf;
            float sourceMaxX = sourceX + sourceHalf;
            float sourceMaxY = sourceY + sourceHalf;

            eeQueryCount += 1;
            IntArray candidates = tileBucketQueryAPI.collectAabb(sourceMinX, sourceMinY, sourceMaxX, sourceMaxY);
            eeCandidateCount += candidates.size;

            for (int i = 0; i < candidates.size; i++) {
                int targetEid = candidates.items[i];
                if (targetEid == sourceEid) {
                    continue;
                }

                int targetBodySlot = collisionBodyStore.slotOf(targetEid);
                if (targetBodySlot < 0) {
                    continue;
                }

                int targetTransformSlot = positionStore.slotOf(targetEid);
                float targetSize = hitboxStore.get(targetEid);
                if (targetTransformSlot < 0 || targetSize <= 0) {
                    continue;
                }

                float targetHalf = targetSize * 0.5f;
                float targetMinX = transformXItems[targetTransformSlot] - targetHalf;
                float targetMinY = transformYItems[targetTransformSlot] - targetHalf;
                float targetMaxX = transformXItems[targetTransformSlot] + targetHalf;
                float targetMaxY = transformYItems[targetTransformSlot] + targetHalf;
                if (!overlaps(sourceMinX, sourceMinY, sourceMaxX, sourceMaxY, targetMinX, targetMinY, targetMaxX, targetMaxY)) {
                    continue;
                }

                eeOverlapCount += 1;
                collisionDispatchAPI.emitUndirectedEe(sourceEid, targetEid);
            }
        }

        collisionDebugStore.eeQueryCount = eeQueryCount;
        collisionDebugStore.eeCandidateCount = eeCandidateCount;
        collisionDebugStore.eeOverlapCount = eeOverlapCount;
    }

    private boolean overlaps(
            float minAx,
            float minAy,
            float maxAx,
            float maxAy,
            float minBx,
            float minBy,
            float maxBx,
            float maxBy
    ) {
        return maxAx > minBx && maxAy > minBy && minAx < maxBx && minAy < maxBy;
    }
}
