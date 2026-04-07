package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;

public class MappingStoreIndexer implements StoreIndexer{
    /** 索引器持有的id集合, 与slot对齐 */
    protected final IntArray ids = new IntArray();
    /** 索引器的id-slot键值对 */
    protected final IntIntMap idToSlot = new IntIntMap();

    @Override
    public int size() {
        return ids.size;
    }

    /**
     * @param id
     * @return slot索引, 若无索引记录, 返回-1
     */
    @Override
    public int slotOf(int id) {
        return idToSlot.get(id, -1);
    }

    @Override
    public int eidOf(int slot) {
        return ids.get(slot);
    }

    @Override
    public InsertResult ensure(int id) {
        int slot = idToSlot.get(id, -1);
        if (slot >= 0) return new InsertResult(slot, false);
        slot = ids.size;
        ids.add(id);
        idToSlot.put(id, slot);
        return new InsertResult(slot, true);
    }

    @Override
    public RemoveResult swapRemove(int id) {
        int slot = idToSlot.get(id, -1);
        if (slot < 0) return new RemoveResult(false, -1, -1);
        int swappedSlot = ids.size - 1;
        if (slot < ids.size - 1) {//要删除的不是尾部slot
            //swap迁移
            int swappedId = ids.get(swappedSlot);
            ids.swap(slot, swappedSlot);
            idToSlot.put(swappedId, slot);
        }
        ids.pop();
        idToSlot.remove(id, -1);
        return new RemoveResult(true, slot, swappedSlot);
    }

    @Override
    public void clear() {
        ids.clear();
        idToSlot.clear();
    }
}
