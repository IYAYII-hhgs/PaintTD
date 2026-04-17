package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class HitboxStore extends ArrayEntityStoreBase {
    public final FloatArray hb = new FloatArray();

    public float get(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? hb.get(slot) : 0;
    }

    public boolean set(int eid, float hitbox) {
        int slot = slotOf(eid);
        if (slot < 0) return false;
        hb.set(slot, hitbox);
        return true;
    }

    public boolean createAndSet(int eid, float hitbox) {
        boolean created = createRow(eid);
        hb.set(slotOf(eid), hitbox);
        return created;
    }

    public void remove(int eid) {
        removeRow(eid);
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        hb.add(0f);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            hb.swap(movedFromSlot, removedSlot);
        }
        hb.pop();
    }

    @Override
    protected void onRowsCleared() {
        hb.clear();
    }
}
