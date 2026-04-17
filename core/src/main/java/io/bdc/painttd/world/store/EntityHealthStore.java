package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class EntityHealthStore extends ArrayEntityStoreBase {
    public final FloatArray maxHealths = new FloatArray();
    public final FloatArray healths = new FloatArray();

    public float getHp(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? healths.get(slot) : 0;
    }

    public float getMaxHp(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? maxHealths.get(slot) : 0;
    }

    public boolean set(int eid, float hp, float maxHp) {
        int slot = slotOf(eid);
        if (slot < 0) return false;
        healths.set(slot, hp);
        maxHealths.set(slot, maxHp);
        return true;
    }

    public boolean create(int eid, float hp, float maxHp) {
        boolean created = createRow(eid);
        if (created) {
            int slot = slotOf(eid);
            healths.set(slot, hp);
            maxHealths.set(slot, maxHp);
        }
        return created;
    }

    public boolean setHp(int eid, float hp) {
        int slot = slotOf(eid);
        if (slot < 0) return false;
        healths.set(slot, hp);
        return true;
    }

    public boolean setMaxHp(int eid, float maxHp) {
        int slot = slotOf(eid);
        if (slot < 0) return false;
        maxHealths.set(slot, maxHp);
        return true;
    }

    public void remove(int eid) {
        removeRow(eid);
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        maxHealths.add(0f);
        healths.add(0f);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            maxHealths.swap(movedFromSlot, removedSlot);
            healths.swap(movedFromSlot, removedSlot);
        }
        maxHealths.pop();
        healths.pop();
    }

    @Override
    protected void onRowsCleared() {
        maxHealths.clear();
        healths.clear();
    }
}
