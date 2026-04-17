package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

import java.util.*;

public abstract class ArrayEntityStoreBase implements WorldStore, EntityOwner {
    public static final int NO_EID = -1;
    public static final int NO_SLOT = -1;
    private static final int MIN_GROWTH = 100;
    public static int MAX_STORE_SIZE = 1<<16;

    private final IntArray eids = new IntArray();
    private int[] slotByEid;

    protected ArrayEntityStoreBase() {
        this(0);
    }

    protected ArrayEntityStoreBase(int initialEidCapacity) {
        if (initialEidCapacity < 0) {
            throw new IllegalArgumentException("initialEidCapacity must be >= 0: " + initialEidCapacity);
        }
        slotByEid = new int[initialEidCapacity];
        Arrays.fill(slotByEid, NO_SLOT);
    }

    public final int size() {
        return eids.size;
    }

    public final int slotOf(int eid) {
        return eid >= 0 && eid < slotByEid.length ? slotByEid[eid] : NO_SLOT;
    }

    public final boolean has(int eid) {
        return slotOf(eid) != NO_SLOT;
    }

    public final int eidOf(int slot) {
        return eids.get(slot);
    }

    public boolean createRow(int eid) {
        if (eid < 0) {
            throw new IllegalArgumentException("eid must be >= 0: " + eid);
        }

        int slot = slotOf(eid);
        if (slot != NO_SLOT) {
            return false;
        }

        ensureEidCapacity(eid + 1);
        slot = eids.size;
        eids.add(eid);
        slotByEid[eid] = slot;
        onRowCreated(eid, slot);
        return true;
    }

    public boolean removeRow(int eid) {
        int slot = slotOf(eid);
        if (slot == NO_SLOT) {
            return false;
        }

        int lastSlot = eids.size - 1;
        int movedEid = slot == lastSlot ? NO_EID : eids.get(lastSlot);
        onRowRemoved(eid, slot, movedEid, movedEid == NO_EID ? NO_SLOT : lastSlot);

        if (movedEid != NO_EID) {
            eids.set(slot, movedEid);
            slotByEid[movedEid] = slot;
        }

        eids.pop();
        slotByEid[eid] = NO_SLOT;
        return true;
    }

    public final void clear() {
        int[] eidItems = eids.items;
        for (int slot = 0, size = eids.size; slot < size; slot++) {
            slotByEid[eidItems[slot]] = NO_SLOT;
        }
        eids.clear();
        onRowsCleared();
    }

    @Override
    public void onEntityDestroy(int entityId) {
        removeRow(entityId);
    }

    /**
     * 在基类完成新行登记并分配稳定 slot 后调用。
     * 子类应在这里为该 slot 追加或初始化自己的列数据。
     */
    protected abstract void onRowCreated(int eid, int slot);

    /**
     * 在基类完成 swap-remove 的索引收尾前调用。
     * 子类应使用传入的 slot 同步执行自己的 swap/pop，不要假定此时 slotOf(movedEid) 已更新。
     */
    protected abstract void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot);

    /**
     * 在基类清空全部 eid-slot 映射和稠密 eid 行后调用。
     * 子类应在这里清空自己的列存储。
     */
    protected abstract void onRowsCleared();

    protected void ensureEidCapacity(int minCapacity) {
        if (slotByEid.length >= minCapacity) {
            return;
        }

        int oldLength = slotByEid.length;
        int newCapacity = nextCapacity(oldLength, minCapacity);
        if (newCapacity > MAX_STORE_SIZE) {
            throw new IllegalStateException("Entity store size limit exceeded: " + MAX_STORE_SIZE);
        }
        slotByEid = Arrays.copyOf(slotByEid, newCapacity);
        Arrays.fill(slotByEid, oldLength, newCapacity, NO_SLOT);
    }

    private int nextCapacity(int currentCapacity, int minCapacity) {
        int capacity = Math.max(currentCapacity, MIN_GROWTH);
        while (capacity < minCapacity) {
            capacity = Math.max(capacity + (capacity >> 1), capacity + MIN_GROWTH);
        }
        return capacity;
    }
}
