package io.bdc.painttd.lib;

import java.util.*;

public class LongSet {
    private static final float DEFAULT_LOAD_FACTOR = 0.8f;

    public int size;

    private long[] keyTable;
    private int mask;
    private int threshold;
    private boolean hasZeroValue;
    private final float loadFactor;

    public LongSet() {
        this(51, DEFAULT_LOAD_FACTOR);
    }

    public LongSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public LongSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity);
        }
        if (loadFactor <= 0f || loadFactor >= 1f) {
            throw new IllegalArgumentException("loadFactor must be in (0, 1): " + loadFactor);
        }

        this.loadFactor = loadFactor;

        int tableSize = tableSize(initialCapacity, loadFactor);
        keyTable = new long[tableSize];
        threshold = Math.min(tableSize - 1, (int) (tableSize * loadFactor));
        mask = tableSize - 1;
    }

    public boolean add(long key) {
        if (key == 0L) {
            if (hasZeroValue) {
                return false;
            }
            hasZeroValue = true;
            size += 1;
            return true;
        }

        int index = locateKey(key);
        if (index >= 0) {
            return false;
        }

        index = -(index + 1);
        keyTable[index] = key;
        size += 1;
        if (size >= threshold) {
            resize(keyTable.length << 1);
        }
        return true;
    }

    public boolean contains(long key) {
        if (key == 0L) {
            return hasZeroValue;
        }
        return locateKey(key) >= 0;
    }

    public boolean remove(long key) {
        if (key == 0L) {
            if (!hasZeroValue) {
                return false;
            }
            hasZeroValue = false;
            size -= 1;
            return true;
        }

        int index = locateKey(key);
        if (index < 0) {
            return false;
        }

        long[] table = keyTable;
        int next = (index + 1) & mask;
        while (table[next] != 0L) {
            long nextKey = table[next];
            int placement = place(nextKey);
            if (((next - placement) & mask) > ((index - placement) & mask)) {
                table[index] = nextKey;
                index = next;
            }
            next = (next + 1) & mask;
        }

        table[index] = 0L;
        size -= 1;
        return true;
    }

    public void ensureCapacity(int additionalCapacity) {
        if (additionalCapacity < 0) {
            throw new IllegalArgumentException("additionalCapacity must be >= 0: " + additionalCapacity);
        }

        int tableSize = tableSize(size + additionalCapacity, loadFactor);
        if (tableSize > keyTable.length) {
            resize(tableSize);
        }
    }

    public void clear() {
        if (size == 0) {
            return;
        }
        Arrays.fill(keyTable, 0L);
        size = 0;
        hasZeroValue = false;
    }

    private int locateKey(long key) {
        long[] table = keyTable;
        int index = place(key);
        while (true) {
            long existing = table[index];
            if (existing == 0L) {
                return -(index + 1);
            }
            if (existing == key) {
                return index;
            }
            index = (index + 1) & mask;
        }
    }

    private int place(long key) {
        long h = key;
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return ((int) h) & mask;
    }

    private void resize(int newSize) {
        long[] oldTable = keyTable;
        keyTable = new long[newSize];
        threshold = Math.min(newSize - 1, (int) (newSize * loadFactor));
        mask = newSize - 1;

        int oldSize = size;
        size = hasZeroValue ? 1 : 0;
        for (int i = 0; i < oldTable.length; i++) {
            long key = oldTable[i];
            if (key != 0L) {
                addResize(key);
                size += 1;
            }
        }
        size = oldSize;
    }

    private void addResize(long key) {
        int index = place(key);
        while (keyTable[index] != 0L) {
            index = (index + 1) & mask;
        }
        keyTable[index] = key;
    }

    private static int tableSize(int capacity, float loadFactor) {
        int target = Math.max(2, (int) Math.ceil(capacity / loadFactor));
        return nextPowerOfTwo(target);
    }

    private static int nextPowerOfTwo(int value) {
        value -= 1;
        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;
        return value + 1;
    }
}
