package io.bdc.painttd.lib;

import com.badlogic.gdx.utils.*;

public class IntHandlePool {
    int capacity;
    final int growthStep;
    final IntArray freeHandles = new IntArray();
    final Bits freeMask = new Bits();

    public IntHandlePool() {
        this(1024);
    }

    public IntHandlePool(int growthStep) {
        if (growthStep <= 0) {
            throw new IllegalArgumentException("growthStep must be > 0: " + growthStep);
        }
        this.growthStep = growthStep;
    }

    /**
     * Total handle range size.
     * Valid handles are in [0, capacity).
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Number of currently free handles.
     */
    public int freeCount() {
        return freeHandles.size;
    }

    /**
     * Number of currently allocated handles.
     */
    public int allocatedCount() {
        return capacity - freeHandles.size;
    }

    public int growthStep() {
        return growthStep;
    }

    public boolean isValid(int handle) {
        return handle >= 0 && handle < capacity;
    }

    public boolean isFree(int handle) {
        return isValid(handle) && freeMask.get(handle);
    }

    public boolean isAllocated(int handle) {
        return isValid(handle) && !freeMask.get(handle);
    }

    /**
     * Allocates one handle.
     * Newly grown capacity returns lower handles first: 0, 1, 2, ...
     */
    public int alloc() {
        if (freeHandles.size == 0) {
            ensureCapacity(capacity + growthStep);
        }

        int handle = freeHandles.pop();
        freeMask.clear(handle);
        return handle;
    }

    /**
     * Ensures the pool can allocate handles in [0, minCapacity).
     */
    public void ensureCapacity(int minCapacity) {
        if (minCapacity <= capacity) {
            return;
        }

        for (int handle = minCapacity - 1; handle >= capacity; handle--) {
            freeHandles.add(handle);
            freeMask.set(handle);
        }

        capacity = minCapacity;
    }

    /**
     * Frees a handle back to the pool.
     */
    public FreeResult tryFree(int handle) {
        if (!isValid(handle)) {
            return FreeResult.INVALID;
        }
        if (freeMask.get(handle)) {
            return FreeResult.ALREADY_FREE;
        }

        freeHandles.add(handle);
        freeMask.set(handle);
        return FreeResult.FREED;
    }

    /**
     * Frees a handle back to the pool, with boolean result.
     */
    public boolean free(int handle) {
        if(!isValid(handle) || freeMask.get(handle)) return false;
        freeHandles.add(handle);
        freeMask.set(handle);
        return true;
    }

    public void clear() {
        capacity = 0;
        freeHandles.clear();
        freeMask.clear();
    }

    public enum FreeResult {
        FREED,
        INVALID,
        ALREADY_FREE
    }
}
