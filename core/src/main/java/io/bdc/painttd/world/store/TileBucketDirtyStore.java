package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class TileBucketDirtyStore implements WorldStore, EntityOwner {
    public final IntArray dirtyEids = new IntArray();
    public final Bits dirtyBits = new Bits();

    public boolean markDirty(int eid) {
        if (eid < 0) {
            return false;
        }
        if (dirtyBits.get(eid)) {
            return false;
        }
        dirtyBits.set(eid);
        dirtyEids.add(eid);
        return true;
    }

    public boolean isDirty(int eid) {
        return eid >= 0 && dirtyBits.get(eid);
    }

    public void clearDirty(int eid) {
        if (eid < 0) {
            return;
        }
        dirtyBits.clear(eid);
    }

    public void clearDirtyList() {
        dirtyEids.clear();
    }

    @Override
    public void onEntityDestroy(int entityId) {
        clearDirty(entityId);
    }
}
