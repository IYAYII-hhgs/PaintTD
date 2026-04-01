package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.def.*;

public class EntityMetaStore implements WorldStore {
    public StoreIndexer indexer = new MappingStoreIndexer();
    public final IntArray entityDefIds = new IntArray();
    public final Array<String> kinds = new Array<>();

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

    public void clear() {
        indexer.clear();
        entityDefIds.clear();
        kinds.clear();
    }

    public void put(int eid, EntityDef entityDef) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (result.created()) {
            entityDefIds.add(entityDef.id);
            kinds.add(entityDef.kind);
            return;
        }
        entityDefIds.set(slot, entityDef.id);
        kinds.set(slot, entityDef.kind);
    }

    public void remove(int eid) {
        var result = indexer.swapRemove(eid);
        if (!result.removed()) {
            return;
        }

        if (result.swapped()) {
            entityDefIds.swap(result.swappedSlot(), result.removedSlot());
            kinds.swap(result.swappedSlot(), result.removedSlot());
        }

        entityDefIds.pop();
        kinds.pop();
    }
}
