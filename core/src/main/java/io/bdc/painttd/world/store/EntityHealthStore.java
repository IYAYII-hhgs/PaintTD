package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class EntityHealthStore implements WorldStore, EntityOwner {
    public StoreIndexer indexer = new MappingStoreIndexer();
    public final FloatArray maxHealths = new FloatArray();
    public final FloatArray healths = new FloatArray();

    public float getHp(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? healths.get(slot) : 0;
    }

    public float getMaxHp(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? maxHealths.get(slot) : 0;
    }

    public void clear() {
        indexer.clear();
        healths.clear();
    }

    public boolean create(int eid, float hp, float maxHp) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (result.created()) {
            maxHealths.add(maxHp);
            healths.add(hp);
            return true;
        }
        return false;
    }

    public void putHp(int eid, float hp) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (!result.created()) {
            healths.set(slot, hp);
        }
    }

    public void putMaxHp(int eid, float maxHp) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (!result.created()) {
            maxHealths.set(slot, maxHp);
        }
    }

    public void remove(int eid) {
        var result = indexer.swapRemove(eid);
        if (!result.removed()) return;
        if (result.swapped()) {
            healths.swap(result.swappedSlot(), result.removedSlot());
        }
        healths.pop();
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
