package io.bdc.painttd.world.family.targeting;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.store.*;

public class EntityTargetingStore extends ArrayEntityStoreBase {
    public IntArray strategyArray = new IntArray();
    public IntArray targetIdArray = new IntArray();

    public enum Targeting {
        HP_MAX,
        HP_MIN,
        DISTANCE_MIN
    }

    public boolean createAndSet(int eid, Targeting strategy) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        strategyArray.set(slot, strategy.ordinal());
        return created;
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        strategyArray.add(0);
        targetIdArray.add(NO_EID);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            strategyArray.swap(movedFromSlot, removedSlot);
            targetIdArray.swap(movedFromSlot, removedSlot);
        }
        strategyArray.pop();
        targetIdArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        strategyArray.clear();
        targetIdArray.clear();
    }
}
