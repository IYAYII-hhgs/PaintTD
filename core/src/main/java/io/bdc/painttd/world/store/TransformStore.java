package io.bdc.painttd.world.store;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class TransformStore implements WorldStore, EntityOwner {
    public StoreIndexer indexer = new MappingStoreIndexer();
    public final FloatArray x = new FloatArray();
    public final FloatArray y = new FloatArray();

    public Vector2 get(int eid, Vector2 out) {
        int slot = slotOf(eid);
        return slot >= 0 ? out.set(x.get(slot), y.get(slot)) : null;
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

    public void putX(int eid, float xValue) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (result.created()) {
            x.add(xValue);
            return;
        }
        x.set(slot, xValue);
    }

    public void putY(int eid, float yValue) {
        var result = indexer.ensure(eid);
        int slot = result.slot();
        if (result.created()) {
            y.add(yValue);
            return;
        }
        y.set(slot, yValue);
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
