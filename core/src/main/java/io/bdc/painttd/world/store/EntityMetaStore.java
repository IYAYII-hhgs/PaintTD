package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.def.*;

/**
 * 实体元数据存储
 * 所有的实体都使用该存储
 */

public class EntityMetaStore implements WorldStore, EntityOwner {
    public StoreIndexer indexer = new MappingStoreIndexer();
    public final Array<EntityDef> entityDefArray = new Array<>();

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
        entityDefArray.clear();
    }

    public void put(int eid, EntityDef entityDef) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (result.created()) {
            entityDefArray.add(entityDef);
            return;
        }
        entityDefArray.set(slot, entityDef);
    }

    public void remove(int eid) {
        var result = indexer.swapRemove(eid);
        if (!result.removed()) {
            return;
        }

        if (result.swapped()) {
            entityDefArray.swap(result.swappedSlot(), result.removedSlot());
        }

        entityDefArray.pop();
    }

    @Override
    public void onEntityDestroy(int entityId) {
        if (!has(entityId)) return;
        remove(entityId);
    }
}
