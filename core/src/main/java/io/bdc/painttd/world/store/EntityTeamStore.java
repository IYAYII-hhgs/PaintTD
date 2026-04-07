package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class EntityTeamStore implements WorldStore, EntityOwner {
    public StoreIndexer indexer = new MappingStoreIndexer();
    public final IntArray teams = new IntArray();

    public float get(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? teams.get(slot) : 0;
    }

    public void clear() {
        indexer.clear();
        teams.clear();
    }

    public void put(int eid, int team) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (result.created()) {
            teams.add(team);
            return;
        }
        teams.set(slot, team);
    }

    public void remove(int eid) {
        var result = indexer.swapRemove(eid);
        if (!result.removed()) return;
        if (result.swapped()) {
            teams.swap(result.swappedSlot(), result.removedSlot());
        }
        teams.pop();
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
