package io.bdc.painttd.world.family.targeting;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.store.*;

public class CellTargetingStore extends ArrayEntityStoreBase {
    public IntArray strategyArray = new IntArray();
    public IntArray targetCellArray = new IntArray();

    public enum CellStrategy {
        TERRITORY_HP_MIN,
        NEAREST_EDGE,
        RANDOM
    }

    public boolean createAndSet(int eid, CellStrategy strategy) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        strategyArray.set(slot, strategy.ordinal());
        return created;
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        strategyArray.add(0);
        targetCellArray.add(-1);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            strategyArray.swap(movedFromSlot, removedSlot);
            targetCellArray.swap(movedFromSlot, removedSlot);
        }
        strategyArray.pop();
        targetCellArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        strategyArray.clear();
        targetCellArray.clear();
    }
}
