package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class CollisionSolidBounceStore extends ArrayEntityStoreBase {
    public static final int HIT_LEFT = 1;
    public static final int HIT_RIGHT = 1 << 1;
    public static final int HIT_DOWN = 1 << 2;
    public static final int HIT_UP = 1 << 3;

    public final IntArray masks = new IntArray();

    public int getMask(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? masks.get(slot) : 0;
    }

    public void addMask(int eid, int addMask) {
        if (addMask == 0) {
            return;
        }

        createRow(eid);
        int slot = slotOf(eid);
        masks.set(slot, masks.get(slot) | addMask);
    }

    public void remove(int eid) {
        removeRow(eid);
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        masks.add(0);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            masks.swap(movedFromSlot, removedSlot);
        }
        masks.pop();
    }

    @Override
    protected void onRowsCleared() {
        masks.clear();
    }
}
