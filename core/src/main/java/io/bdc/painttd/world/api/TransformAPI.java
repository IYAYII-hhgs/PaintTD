package io.bdc.painttd.world.api;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

/**
 * 本 API 提供坐标变换数据的统一写入口。
 */
public class TransformAPI implements WorldAPI {
    public PositionStore positionStore;
    public HitboxStore hitboxStore;

    public TileBucketDirtyQueue dirtyStore;

    @Override
    public void onBind(WorldAccess binder) {
        positionStore = binder.getStore(PositionStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
        dirtyStore = binder.getStore(TileBucketDirtyQueue.class);
    }

    /** 热点写入可以直写数组, 并手动使用该方法提醒格子桶. */
    public void markDirty(int eid) {
        dirtyStore.markDirty(eid);
    }

    public boolean setPosition(int eid, float x, float y) {
        int slot = positionStore.slotOf(eid);
        if (slot < 0) {
            return false;
        }
        if (positionStore.x.items[slot] == x && positionStore.y.items[slot] == y) {
            return true;
        }
        positionStore.x.items[slot] = x;
        positionStore.y.items[slot] = y;
        dirtyStore.markDirty(eid);
        return true;
    }

    public boolean setPosX(int eid, float x) {
        int slot = positionStore.slotOf(eid);
        if (slot < 0) {
            return false;
        }
        if (positionStore.x.items[slot] == x) {
            return true;
        }
        positionStore.x.items[slot] = x;
        dirtyStore.markDirty(eid);
        return true;
    }

    public boolean setPosY(int eid, float y) {
        int slot = positionStore.slotOf(eid);
        if (slot < 0) {
            return false;
        }
        if (positionStore.y.items[slot] == y) {
            return true;
        }
        positionStore.y.items[slot] = y;
        dirtyStore.markDirty(eid);
        return true;
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

    public boolean createAndSetPosition(int eid, float x, float y) {
        boolean created = positionStore.createRow(eid);
        int slot = positionStore.slotOf(eid);
        positionStore.x.items[slot] = x;
        positionStore.y.items[slot] = y;
        dirtyStore.markDirty(eid);
        return created;
    }

    public boolean createAndSetHitbox(int eid, float size) {
        hitboxStore.createRow(eid);
        int slot = hitboxStore.slotOf(eid);
        hitboxStore.hb.items[slot] = size;
        dirtyStore.markDirty(eid);
        return true;
    }
}
