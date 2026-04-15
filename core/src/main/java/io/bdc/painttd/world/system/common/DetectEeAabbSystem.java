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
 * 只产出两个实体接触事实, 传递给有向碰撞和无向碰撞分发层, 不处理应用效果.
 */
public class DetectEeAabbSystem extends WorldSystem {
    public CollisionBodyStore collisionBodyStore;
    public TransformStore transformStore;
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
        transformStore = binder.getStore(TransformStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
        tileBucketQueryAPI = binder.getApi(TileBucketQueryAPI.class);
        collisionDispatchAPI = binder.getApi(CollisionDispatchAPI.class);
        collisionDebugStore = binder.getStore(CollisionDebugStore.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < collisionBodyStore.size(); slot++) {
            int sourceEid = collisionBodyStore.eidOf(slot);
            if (!collisionBodyStore.isDynamic(sourceEid)) {
                continue;
            }
            if (!transformStore.has(sourceEid) || !hitboxStore.has(sourceEid)) {
                continue;
            }

            float sourceSize = hitboxStore.get(sourceEid);
            if (sourceSize <= 0f) {
                continue;
            }

            float sourceX = transformStore.getX(sourceEid);
            float sourceY = transformStore.getY(sourceEid);
            float sourceHalf = sourceSize * 0.5f;
            float sourceMinX = sourceX - sourceHalf;
            float sourceMinY = sourceY - sourceHalf;
            float sourceMaxX = sourceX + sourceHalf;
            float sourceMaxY = sourceY + sourceHalf;

            collisionDebugStore.eeQueryCount += 1;
            IntArray candidates = tileBucketQueryAPI.collectAabb(sourceMinX, sourceMinY, sourceMaxX, sourceMaxY);
            collisionDebugStore.eeCandidateCount += candidates.size;

            for (int i = 0; i < candidates.size; i++) {
                int targetEid = candidates.get(i);
                if (targetEid == sourceEid) {
                    continue;
                }
                if (!collisionBodyStore.has(targetEid)) {
                    continue;
                }
                if (!transformStore.has(targetEid) || !hitboxStore.has(targetEid)) {
                    continue;
                }

                float targetSize = hitboxStore.get(targetEid);
                if (targetSize <= 0f) {
                    continue;
                }

                float targetX = transformStore.getX(targetEid);
                float targetY = transformStore.getY(targetEid);
                float targetHalf = targetSize * 0.5f;
                float targetMinX = targetX - targetHalf;
                float targetMinY = targetY - targetHalf;
                float targetMaxX = targetX + targetHalf;
                float targetMaxY = targetY + targetHalf;
                if (!overlaps(sourceMinX, sourceMinY, sourceMaxX, sourceMaxY, targetMinX, targetMinY, targetMaxX, targetMaxY)) {
                    continue;
                }

                collisionDebugStore.eeOverlapCount += 1;
                collisionDispatchAPI.emitUndirectedEe(sourceEid, targetEid);
            }
        }
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
