package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class CollisionBodyStore extends ArrayEntityStoreBase {
    public static final int BODY_NONE = 0;
    public static final int BODY_DYNAMIC = 1;
    public static final int BODY_STATIC = 2;

    public final IntArray bodyTypes = new IntArray();

    public int get(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? bodyTypes.get(slot) : BODY_NONE;
    }

    public boolean isDynamic(int eid) {
        return get(eid) == BODY_DYNAMIC;
    }

    public boolean isStatic(int eid) {
        return get(eid) == BODY_STATIC;
    }

    public boolean set(int eid, int bodyType) {
        int slot = slotOf(eid);
        if (slot < 0) return false;
        bodyTypes.set(slot, bodyType);
        return true;
    }

    public boolean createAndSet(int eid, int bodyType) {
        boolean created = createRow(eid);
        bodyTypes.set(slotOf(eid), bodyType);
        return created;
    }

    public void remove(int eid) {
        removeRow(eid);
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        bodyTypes.add(BODY_NONE);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            bodyTypes.swap(movedFromSlot, removedSlot);
        }
        bodyTypes.pop();
    }

    @Override
    protected void onRowsCleared() {
        bodyTypes.clear();
    }
}
