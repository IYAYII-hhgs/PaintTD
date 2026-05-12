package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class DestroyQueue implements WorldStore {
    /** DO NOT ADD TO ARRAY DIRECTLY */
    public final IntArray eids = new IntArray();
    public final Bits added = new Bits();

    public int lastDestroy;

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
        if (!added.getAndSet(eid)) {
            eids.add(eid);
        }
    }

    public void clear() {
        lastDestroy = size();
        eids.clear();
        added.clear();
    }
}
