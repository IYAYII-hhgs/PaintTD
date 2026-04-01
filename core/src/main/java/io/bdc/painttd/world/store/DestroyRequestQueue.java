package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class DestroyRequestQueue implements WorldStore {
    public final IntArray eids = new IntArray();

    public int size() {
        return eids.size;
    }

    public void add(int eid) {
        eids.add(eid);
    }

    public void clear() {
        eids.clear();
    }
}
