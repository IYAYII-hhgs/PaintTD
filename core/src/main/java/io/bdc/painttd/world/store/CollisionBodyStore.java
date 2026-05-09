package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class CollisionBodyStore extends ArrayEntityStoreBase {
    public static final int BODY_NONE = 0;
    public static final int BODY_DYNAMIC = 1;
    public static final int BODY_STATIC = 2;

    public static final int CAT_UNIT = 1 << 0;
    public static final int CAT_BUILDING = 1 << 1;
    public static final int CAT_BULLET = 1 << 2;
    public static final int CAT_MAP = 1 << 3;
    public static int CAT_MAPMASK = CAT_BULLET | CAT_UNIT | CAT_BUILDING;
    public static int CAT_ALLMASK = CAT_UNIT | CAT_BUILDING | CAT_BULLET | CAT_MAP;

    public final IntArray bodyTypeArray = new IntArray();
    // 碰撞类型
    public final IntArray categoryArray = new IntArray();
    // 碰撞遮罩：接受的来源类型
    public final IntArray categoryMaskArray = new IntArray();

    public int get(int eid) {
        int slot = slotOf(eid);
        return slot >= 0 ? bodyTypeArray.get(slot) : BODY_NONE;
    }

    public boolean isDynamic(int eid) {
        return get(eid) == BODY_DYNAMIC;
    }

    public boolean isStatic(int eid) {
        return get(eid) == BODY_STATIC;
    }

    public boolean createAndSet(int eid, int bodyType, int category, int mask) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        bodyTypeArray.set(slot, bodyType);
        categoryArray.set(slot, category);
        categoryMaskArray.set(slot, mask);
        return created;
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        bodyTypeArray.add(BODY_NONE);
        categoryArray.add(0);
        categoryMaskArray.add(0);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            bodyTypeArray.swap(movedFromSlot, removedSlot);
            categoryArray.swap(movedFromSlot, removedSlot);
            categoryMaskArray.swap(movedFromSlot, removedSlot);
        }
        bodyTypeArray.pop();
        categoryArray.pop();
        categoryMaskArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        bodyTypeArray.clear();
        categoryArray.clear();
        categoryMaskArray.clear();
    }
}
