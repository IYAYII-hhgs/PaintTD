package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class TileBucketDirtyQueue implements WorldStore, EntityOwner {
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

    public void clear(int eid) {
        if (eid < 0) {
            return;
        }
        dirtyBits.clear(eid);
    }

    public void clear() {
        dirtyEids.clear();
    }

    @Override
    public void onEntityDestroy(int entityId) {
        clear(entityId);
    }
}
