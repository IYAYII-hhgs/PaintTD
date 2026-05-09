package io.bdc.painttd.world.family.bullet;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.store.*;

public class BulletStore extends ArrayEntityStoreBase {
    public IntArray sourceArray = new IntArray();
    public IntArray targetEntityArray = new IntArray();
    public FloatArray maxSpeedArray = new FloatArray();
    public FloatArray damageArray = new FloatArray();

    public boolean createAndSet(int eid, int sourceEid, int targetEid) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        sourceArray.set(slot, sourceEid);
        targetEntityArray.set(slot, targetEid);
        maxSpeedArray.set(slot, 1f/60f);
        return created;
    }

    public void setSpeed(int eid, float speed) {
        int slot = slotOf(eid);
        maxSpeedArray.set(slot, speed);
    }

    public void setDamage(int eid, float damage) {
        int slot = slotOf(eid);
        this.damageArray.set(slot, damage);
    }

    @Override
    public void onEntityDestroy(int entityId) {
        super.onEntityDestroy(entityId);
        // 如果某个子弹的目标是该实体，应将目标标记为空（-1）
        for (int i = 0; i < targetEntityArray.size; i++) {
            if (targetEntityArray.get(i) == entityId) {
                targetEntityArray.set(i, NO_EID);
            }
        }
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        sourceArray.add(NO_EID);
        targetEntityArray.add(NO_EID);
        maxSpeedArray.add(1f/60f);
        damageArray.add(0f);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            sourceArray.swap(movedFromSlot, removedSlot);
            targetEntityArray.swap(movedFromSlot, removedSlot);
            maxSpeedArray.swap(movedFromSlot, removedSlot);
            damageArray.swap(movedFromSlot, removedSlot);
        }
        sourceArray.pop();
        targetEntityArray.pop();
        maxSpeedArray.pop();
        damageArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        sourceArray.clear();
        targetEntityArray.clear();
        maxSpeedArray.clear();
        damageArray.clear();
    }
}
