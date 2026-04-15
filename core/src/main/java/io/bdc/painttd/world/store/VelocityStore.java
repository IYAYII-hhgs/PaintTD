package io.bdc.painttd.world.store;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class VelocityStore implements WorldStore, EntityOwner {
    public StoreIndexer indexer = new MappingStoreIndexer();
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

    public int size() {
        return indexer.size();
    }

    public int slotOf(int eid) {
        return indexer.slotOf(eid);
    }

    public int eidOf(int slot) {
        return indexer.eidOf(slot);
    }

    public boolean has(int eid) {
        return indexer.has(eid);
    }

    public void clear() {
        indexer.clear();
        x.clear();
        y.clear();
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

    public void put(int eid, float xValue, float yValue) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (result.created()) {
            x.add(xValue);
            y.add(yValue);
            return;
        }
        x.set(slot, xValue);
        y.set(slot, yValue);
    }

    public void put(int eid, Vector2 pos) {
        put(eid, pos.x, pos.y);
    }

    public void remove(int eid) {
        var result = indexer.swapRemove(eid);
        if (!result.removed()) return;
        if (result.swapped()) {
            x.swap(result.swappedSlot(), result.removedSlot());
            y.swap(result.swappedSlot(), result.removedSlot());
        }
        x.pop();
        y.pop();
    }

    @Override
    public void onEntityDestroy(int entityId) {
        if (!has(entityId)) return;
        remove(entityId);
    }
}
