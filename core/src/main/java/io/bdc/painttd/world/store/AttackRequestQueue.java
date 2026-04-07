package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class AttackRequestQueue implements WorldStore {
    public final IntArray eids = new IntArray();

    public int size() {
        return eids.size;
    }

    /**
     * Queues an entity id for recycle processing.
     * <p>
     * Queueing an entity id that is already absent from the relevant stores is allowed; recycle
     * processing will treat that destroy request as a no-op.
     */
    public void add(int eid) {
        eids.add(eid);
    }

    public void clear() {
        eids.clear();
    }
}
