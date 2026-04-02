package io.bdc.painttd.world;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;

/**
 * 运行时实体Id池.
 */

public class EntityIdManager {
    int size;
    final IntArray freeIds = new IntArray();
    final Bits inPool = new Bits();

    public int size() {
        return size;
    }

    public int alloc() {
        if (freeIds.size == 0) {
            ensurePool(size + 1000);
        }
        int id = freeIds.pop();
        inPool.clear(id);
        return id;
    }

    public void free(int id) {
        if (id >= size || id < 0) {
            PaintTD.log.info("Invalid freeing entity id: " + id);
            return;
        }
        if (!inPool.get(id)) {
            freeIds.add(id);
            inPool.set(id);
        } else {
            PaintTD.log.info("Duplicate freeing entity id: " + id);
        }
    }

    public void ensurePool(int newSize) {
        if (size < newSize) {
            for (int i = newSize - 1; i >= size; i--) {
                freeIds.add(i);
                inPool.set(i);
            }
            size = newSize;
        }
    }

    public void clear() {
        size = 0;
        freeIds.clear();
        inPool.clear();
    }
}
