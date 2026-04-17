package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

/**
 * 静态阻挡响应的中间态 store。
 * 为每个实体累计本帧的 side mask 与四方向最大推出量，
 * 供 CollisionSolidBounceSystem 在统一 apply 阶段先解穿透、再修正速度。
 */
public class CollisionSolidBounceStore extends ArrayEntityStoreBase {
    public static final int HIT_LEFT = 1;
    public static final int HIT_RIGHT = 1 << 1;
    public static final int HIT_DOWN = 1 << 2;
    public static final int HIT_UP = 1 << 3;

    public final IntArray masks = new IntArray();
    public final FloatArray pushLeft = new FloatArray();
    public final FloatArray pushRight = new FloatArray();
    public final FloatArray pushDown = new FloatArray();
    public final FloatArray pushUp = new FloatArray();

    public int getMask(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? masks.get(slot) : 0;
    }

    public void addMask(int eid, int addMask) {
        if (addMask == 0) {
            return;
        }

        createRow(eid);
        int slot = slotOf(eid);
        masks.set(slot, masks.get(slot) | addMask);
    }

    public void addResponse(int eid, int addMask, float addPushLeft, float addPushRight, float addPushDown, float addPushUp) {
        if (addMask == 0
                && addPushLeft <= 0f
                && addPushRight <= 0f
                && addPushDown <= 0f
                && addPushUp <= 0f) {
            return;
        }

        createRow(eid);
        int slot = slotOf(eid);
        masks.items[slot] |= addMask;
        if (addPushLeft > pushLeft.items[slot]) {
            pushLeft.items[slot] = addPushLeft;
        }
        if (addPushRight > pushRight.items[slot]) {
            pushRight.items[slot] = addPushRight;
        }
        if (addPushDown > pushDown.items[slot]) {
            pushDown.items[slot] = addPushDown;
        }
        if (addPushUp > pushUp.items[slot]) {
            pushUp.items[slot] = addPushUp;
        }
    }

    public void remove(int eid) {
        removeRow(eid);
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        masks.add(0);
        pushLeft.add(0f);
        pushRight.add(0f);
        pushDown.add(0f);
        pushUp.add(0f);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            masks.swap(movedFromSlot, removedSlot);
            pushLeft.swap(movedFromSlot, removedSlot);
            pushRight.swap(movedFromSlot, removedSlot);
            pushDown.swap(movedFromSlot, removedSlot);
            pushUp.swap(movedFromSlot, removedSlot);
        }
        masks.pop();
        pushLeft.pop();
        pushRight.pop();
        pushDown.pop();
        pushUp.pop();
    }

    @Override
    protected void onRowsCleared() {
        masks.clear();
        pushLeft.clear();
        pushRight.clear();
        pushDown.clear();
        pushUp.clear();
    }
}
