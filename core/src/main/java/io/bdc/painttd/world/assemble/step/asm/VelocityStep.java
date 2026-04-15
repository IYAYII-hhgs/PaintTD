package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.store.request.*;

public class VelocityStep implements AssembleStep {
    public static final String TYPE = "VelocityStep";

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var store = binder.getStore(VelocityStore.class);
        if (store == null) {
            throw new IllegalStateException("Target store is missed.");
        }
        store.put(eid, 0, 0);
    }
}
