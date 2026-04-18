package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

/**
 * TileBucket 的同步 barrier。
 * 当前实现采用 dirty eid 增量同步方式，
 * 在进入碰撞与查询阶段前只刷新本帧被标记为 dirty 的覆盖关系。
 * <p>
 * 每个 dirty eid 在一次 barrier 中最多只会被同步一次；
 * 若实体已缺失位置或命中盒，或命中盒尺寸非法，则会从 bucket 中卸载。 
 */
public class TileBucketSyncSystem extends WorldSystem {
    public TileBucketStore bucketStore;
    public TileBucketDebugStore debugStore;
    public TileBucketDirtyStore dirtyStore;
    public TransformStore transformStore;
    public HitboxStore hitboxStore;

    public TileBucketSyncSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        bucketStore = binder.getStore(TileBucketStore.class);
        debugStore = binder.getStore(TileBucketDebugStore.class);
        dirtyStore = binder.getStore(TileBucketDirtyStore.class);
        transformStore = binder.getStore(TransformStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
    }

    @Override
    public void run(float delta) {
        int[] dirtyItems = dirtyStore.dirtyEids.items;
        float[] transformXItems = transformStore.x.items;
        float[] transformYItems = transformStore.y.items;
        float[] hitboxItems = hitboxStore.hb.items;
        int dirtyQueuedCount = dirtyStore.dirtyEids.size;
        int dirtyFlushedCount = 0;
        int dirtyRemovedCount = 0;
        int dirtyReinsertCount = 0;

        for (int i = 0; i < dirtyStore.dirtyEids.size; i++) {
            int eid = dirtyItems[i];
            if (!dirtyStore.isDirty(eid)) {
                continue;
            }
            dirtyStore.clearDirty(eid);
            dirtyFlushedCount += 1;

            int transformSlot = transformStore.slotOf(eid);
            int hitboxSlot = hitboxStore.slotOf(eid);
            if (transformSlot < 0 || hitboxSlot < 0) {
                bucketStore.removeEntity(eid);
                dirtyRemovedCount += 1;
                continue;
            }

            float size = hitboxItems[hitboxSlot];
            if (size <= 0f) {
                bucketStore.removeEntity(eid);
                dirtyRemovedCount += 1;
                continue;
            }

            float x = transformXItems[transformSlot];
            float y = transformYItems[transformSlot];
            bucketStore.reinsertSquare(eid, x, y, size);
            dirtyReinsertCount += 1;
        }

        dirtyStore.clearDirtyList();
        debugStore.dirtyQueuedCount = dirtyQueuedCount;
        debugStore.dirtyFlushedCount = dirtyFlushedCount;
        debugStore.dirtyRemovedCount = dirtyRemovedCount;
        debugStore.dirtyReinsertCount = dirtyReinsertCount;
    }
}
