package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.def.*;

/**
 * 实体元数据存储
 * 所有的实体都使用该存储
 */

public class EntityMetaStore extends ArrayEntityStoreBase {
    public final Array<EntityDef> entityDefArray = new Array<>(false, 16);

    public boolean set(int eid, EntityDef entityDef) {
        int slot = slotOf(eid);
        if (slot < 0) return false;
        entityDefArray.set(slot, entityDef);
        return true;
    }

    public EntityDef get(int eid) {
        int slot = slotOf(eid);
        if (slot < 0) return null;
        return entityDefArray.get(slot);
    }

    public boolean createAndSet(int eid, EntityDef entityDef) {
        boolean created = createRow(eid);
        entityDefArray.set(slotOf(eid), entityDef);
        return created;
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        entityDefArray.add(null);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            entityDefArray.swap(movedFromSlot, removedSlot);
        }
        entityDefArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        entityDefArray.clear();
    }
}
