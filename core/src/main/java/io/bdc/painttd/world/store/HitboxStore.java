package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class HitboxStore implements WorldStore, EntityOwner {
    public StoreIndexer indexer = new MappingStoreIndexer();
    public final FloatArray hb = new FloatArray();

    public float get(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? hb.get(slot) : 0;
    }

    public void clear() {
        indexer.clear();
        hb.clear();
    }

    public void put(int eid, float hitbox) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (result.created()) {
            hb.add(hitbox);
            return;
        }
        hb.set(slot, hitbox);
    }

    public void remove(int eid) {
        var result = indexer.swapRemove(eid);
        if (!result.removed()) return;
        if (result.swapped()) {
            hb.swap(result.swappedSlot(), result.removedSlot());
        }
        hb.pop();
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
