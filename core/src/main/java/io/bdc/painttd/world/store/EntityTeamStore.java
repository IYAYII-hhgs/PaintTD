package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class EntityTeamStore extends ArrayEntityStoreBase {
    public final IntArray teams = new IntArray();

    public float get(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? teams.get(slot) : 0;
    }

    public boolean set(int eid, int team) {
        int slot = slotOf(eid);
        if (slot < 0) return false;
        teams.set(slot, team);
        return true;
    }

    public boolean createAndSet(int eid, int team) {
        boolean created = createRow(eid);
        teams.set(slotOf(eid), team);
        return created;
    }

    public void remove(int eid) {
        removeRow(eid);
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        teams.add(0);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            teams.swap(movedFromSlot, removedSlot);
        }
        teams.pop();
    }

    @Override
    protected void onRowsCleared() {
        teams.clear();
    }
}
