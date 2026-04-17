package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

/**
 * 当前实现直接按 TransformStore + HitboxStore 每帧全量重建 TileBucket，
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
