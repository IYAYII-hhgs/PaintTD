package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class CollisionBodyStore implements WorldStore, EntityOwner {
    public static final int BODY_NONE = 0;
    public static final int BODY_DYNAMIC = 1;
    public static final int BODY_STATIC = 2;

    public StoreIndexer indexer = new MappingStoreIndexer();
    public final IntArray bodyTypes = new IntArray();

    public int get(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? bodyTypes.get(slot) : BODY_NONE;
    }

    public boolean isDynamic(int eid) {
        return get(eid) == BODY_DYNAMIC;
    }

    public boolean isStatic(int eid) {
        return get(eid) == BODY_STATIC;
    }

    public void clear() {
        indexer.clear();
        bodyTypes.clear();
    }

    public void put(int eid, int bodyType) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (result.created()) {
            bodyTypes.add(bodyType);
            return;
        }
        bodyTypes.set(slot, bodyType);
    }

    public void remove(int eid) {
        var result = indexer.swapRemove(eid);
        if (!result.removed()) return;
        if (result.swapped()) {
            bodyTypes.swap(result.swappedSlot(), result.removedSlot());
        }
        bodyTypes.pop();
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
