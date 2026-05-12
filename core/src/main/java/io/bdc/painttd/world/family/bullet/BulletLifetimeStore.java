package io.bdc.painttd.world.family.bullet;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.store.*;

public class BulletLifetimeStore extends ArrayEntityStoreBase {
    public static int HIT_COOLDOWN = 30;

    public FloatArray durationArray = new FloatArray();
    public IntArray lastHitTickArray = new IntArray();
    public FloatArray lifeLeftArray = new FloatArray();

    public boolean createAndSet(int eid, float duration) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        durationArray.set(slot, duration);
        return created;
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        durationArray.add(10f);
        lastHitTickArray.add(0);
        lifeLeftArray.add(1800f);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            durationArray.swap(movedFromSlot, removedSlot);
            lastHitTickArray.swap(movedFromSlot, removedSlot);
            lifeLeftArray.swap(movedFromSlot, removedSlot);
        }
        durationArray.pop();
        lastHitTickArray.pop();
        lifeLeftArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        durationArray.clear();
        lastHitTickArray.clear();
        lifeLeftArray.clear();
    }
}
