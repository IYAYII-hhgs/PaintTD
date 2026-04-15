package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class CollisionSolidBounceStore implements WorldStore, EntityOwner {
    public static final int HIT_LEFT = 1;
    public static final int HIT_RIGHT = 1 << 1;
    public static final int HIT_DOWN = 1 << 2;
    public static final int HIT_UP = 1 << 3;

    public StoreIndexer indexer = new MappingStoreIndexer();
    public final IntArray masks = new IntArray();

    public int getMask(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? masks.get(slot) : 0;
    }

    public void addMask(int eid, int addMask) {
        if (addMask == 0) {
            return;
        }

        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (result.created()) {
            masks.add(addMask);
            return;
        }
        masks.set(slot, masks.get(slot) | addMask);
    }

    public void clear() {
        indexer.clear();
        masks.clear();
    }

    public void remove(int eid) {
        var result = indexer.swapRemove(eid);
        if (!result.removed()) return;
        if (result.swapped()) {
            masks.swap(result.swappedSlot(), result.removedSlot());
        }
        masks.pop();
    }

    public int size() {
        return indexer.size();
    }

    public int slotOf(int eid) {
        return indexer.slotOf(eid);
    }

    public int eidOf(int slot) {
        return indexer.eidOf(slot);
    }

    public boolean has(int eid) {
        return indexer.has(eid);
    }

    @Override
    public void onEntityDestroy(int entityId) {
        if (!has(entityId)) return;
        remove(entityId);
    }
}
