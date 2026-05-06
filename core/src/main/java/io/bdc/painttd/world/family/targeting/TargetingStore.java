package io.bdc.painttd.world.family.targeting;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.store.*;

@Deprecated
public class TargetingStore extends ArrayEntityStoreBase {
    public Array<Targeting> targetingArray = new Array<>();

    public BooleanArray isCellArray = new BooleanArray();
    public IntArray targetIdArray = new IntArray();

    public boolean createAndSet(int eid, Targeting targeting) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        targetingArray.set(slot, targeting);
        return created;
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        targetingArray.add(null);
        isCellArray.add(false);
        targetIdArray.add(NO_EID);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            targetingArray.swap(movedFromSlot, removedSlot);
            isCellArray.swap(movedFromSlot, removedSlot);
            targetIdArray.swap(movedFromSlot, removedSlot);
        }
        targetingArray.pop();
        isCellArray.pop();
        targetIdArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        targetingArray.clear();
        isCellArray.clear();
        targetIdArray.clear();
    }

    public enum TargetType {
    }

    public enum Targeting {
        DISTANCE_MIN,
        DISTANCE_MAX,
        HP_MIN,
        HP_MAX
    }
}
