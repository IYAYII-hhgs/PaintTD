package io.bdc.painttd.world.store;

public interface StoreIndexer {
    int size();

    int slotOf(int id);

    default boolean has(int id) {
        return slotOf(id) >= 0;
    }

    int eidOf(int slot);

    InsertResult ensure(int id);

    RemoveResult swapRemove(int id);

    void clear();

    record InsertResult(
        int slot,//修改的slot
        boolean created//是否发生创建
    ) {
    }

    record RemoveResult(
        boolean removed,//是否发生删除
        int removedSlot,//被删除的slot
        int swappedSlot//被迁移的slot
    ) {
        public boolean swapped() {
            return removed && removedSlot != swappedSlot;
        }
    }
}
