package io.bdc.painttd.world;

import io.bdc.painttd.*;
import io.bdc.painttd.lib.*;

/**
 * 运行时实体Id池.
 */

public class EntityIdManager extends IntHandlePool {
    public static final int NONE = -1;

    @Override
    public boolean free(int handle) {
        boolean signal = super.free(handle);
        if (!signal) {
            PaintTD.log.error("EntityIdManager: free failed, handle: " + handle);
        }
        return signal;
    }
}
