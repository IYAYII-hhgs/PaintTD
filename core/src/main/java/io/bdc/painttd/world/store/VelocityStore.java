package io.bdc.painttd.world.store;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class VelocityStore extends ArrayEntityStoreBase {
    public final FloatArray x = new FloatArray();
    public final FloatArray y = new FloatArray();

    public Vector2 get(int eid, Vector2 out) {
        int slot = slotOf(eid);
        return slot >= 0 ? out.set(x.get(slot), y.get(slot)) : null;
    }

    public float getX(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? x.get(slot) : 0;
    }

    public float getY(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? y.get(slot) : 0;
    }

    public boolean set(int eid, float xValue, float yValue) {
        int slot = slotOf(eid);
        if (slot < 0) return false;
        x.set(slot, xValue);
        y.set(slot, yValue);
        return true;
    }

    public boolean setX(int eid, float xValue) {
        int slot = slotOf(eid);
        if (slot < 0) return false;
        x.set(slot, xValue);
        return true;
    }

    public boolean setY(int eid, float yValue) {
        int slot = slotOf(eid);
        if (slot < 0) return false;
        y.set(slot, yValue);
        return true;
    }

    public boolean createAndSet(int eid, float xValue, float yValue) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        x.set(slot, xValue);
        y.set(slot, yValue);
        return created;
    }

    public boolean createAndSet(int eid, Vector2 pos) {
        return createAndSet(eid, pos.x, pos.y);
    }

    public void remove(int eid) {
        removeRow(eid);
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        x.add(0f);
        y.add(0f);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            x.swap(movedFromSlot, removedSlot);
            y.swap(movedFromSlot, removedSlot);
        }
        x.pop();
        y.pop();
    }

    @Override
    protected void onRowsCleared() {
        x.clear();
        y.clear();
    }
}
