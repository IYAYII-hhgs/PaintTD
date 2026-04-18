package io.bdc.painttd.world.api;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

/**
 * 本 API 提供命中盒的统一写入口。
 */
public class HitboxAPI implements WorldAPI {
    public HitboxStore hitboxStore;
    public TileBucketDirtyStore dirtyStore;

    @Override
    public void onBind(WorldAccess binder) {
        hitboxStore = binder.getStore(HitboxStore.class);
        dirtyStore = binder.getStore(TileBucketDirtyStore.class);
    }

    public boolean setHitbox(int eid, float size) {
        int slot = hitboxStore.slotOf(eid);
        if (slot < 0) {
            return false;
        }
        if (hitboxStore.hb.items[slot] == size) {
            return true;
        }
        hitboxStore.hb.items[slot] = size;
        dirtyStore.markDirty(eid);
        return true;
    }

    public boolean createAndSetHitbox(int eid, float size) {
        hitboxStore.createRow(eid);
        int slot = hitboxStore.slotOf(eid);
        hitboxStore.hb.items[slot] = size;
        dirtyStore.markDirty(eid);
        return true;
    }

    public boolean removeHitbox(int eid) {
        boolean removed = hitboxStore.removeRow(eid);
        if (removed) {
            dirtyStore.markDirty(eid);
        }
        return removed;
    }
}
