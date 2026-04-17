package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

/**
 * TileBucket 的同步 barrier。
 * 当前实现采用每帧 `clear + rebuild` 的保守同步方式，
 * 在进入碰撞与查询阶段前把 TransformStore 与 HitboxStore 的当前状态重建到 bucket 中。
 * <p>
 * 文档中讨论过的 `flushDirty` / 增量同步仍保留为后续优化方向，当前版本尚未落地到代码。 
 */
public class TileBucketSyncSystem extends WorldSystem {
    public TileBucketStore bucketStore;
    public TransformStore transformStore;
    public HitboxStore hitboxStore;

    public TileBucketSyncSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        bucketStore = binder.getStore(TileBucketStore.class);
        transformStore = binder.getStore(TransformStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
    }

    @Override
    public void run(float delta) {
        bucketStore.clear();

        for (int slot = 0; slot < transformStore.size(); slot++) {
            int eid = transformStore.eidOf(slot);
            if (!hitboxStore.has(eid)) {
                continue;
            }

            float size = hitboxStore.get(eid);
            if (size <= 0f) {
                continue;
            }

            float x = transformStore.x.get(slot);
            float y = transformStore.y.get(slot);
            bucketStore.reinsertSquare(eid, x, y, size);
        }
    }
}
