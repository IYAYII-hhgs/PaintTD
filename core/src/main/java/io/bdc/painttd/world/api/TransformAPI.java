package io.bdc.painttd.world.api;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

/**
 * 本 API 提供位置的统一写入口。
 */
public class TransformAPI implements WorldAPI {
    public TransformStore transformStore;
    public TileBucketDirtyStore dirtyStore;

    @Override
    public void onBind(WorldAccess binder) {
        transformStore = binder.getStore(TransformStore.class);
        dirtyStore = binder.getStore(TileBucketDirtyStore.class);
    }

    public boolean setTransform(int eid, float x, float y) {
        int slot = transformStore.slotOf(eid);
        if (slot < 0) {
            return false;
        }
        if (transformStore.x.items[slot] == x && transformStore.y.items[slot] == y) {
            return true;
        }
        transformStore.x.items[slot] = x;
        transformStore.y.items[slot] = y;
        dirtyStore.markDirty(eid);
        return true;
    }

    public boolean createAndSetTransform(int eid, float x, float y) {
        boolean created = transformStore.createRow(eid);
        int slot = transformStore.slotOf(eid);
        transformStore.x.items[slot] = x;
        transformStore.y.items[slot] = y;
        dirtyStore.markDirty(eid);
        return created;
    }

    public boolean setX(int eid, float x) {
        int slot = transformStore.slotOf(eid);
        if (slot < 0) {
            return false;
        }
        if (transformStore.x.items[slot] == x) {
            return true;
        }
        transformStore.x.items[slot] = x;
        dirtyStore.markDirty(eid);
        return true;
    }

    public boolean setY(int eid, float y) {
        int slot = transformStore.slotOf(eid);
        if (slot < 0) {
            return false;
        }
        if (transformStore.y.items[slot] == y) {
            return true;
        }
        transformStore.y.items[slot] = y;
        dirtyStore.markDirty(eid);
        return true;
    }

    /**
     * 热点写入可以直写数组, 并使用该方法标记脏.
     */
    public void markDirty(int eid) {
        dirtyStore.markDirty(eid);
    }
}
