package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.store.request.*;

public final class EntityMetaStep implements AssembleStep {
    public static final String TYPE = "EntityMetaStep";

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        EntityDef entityDef = req.entityDef;
        if (entityDef == null) {
            throw new IllegalStateException("EntitySpawnRequest.entityDef is required.");
        }
        binder.getStore(EntityMetaStore.class).createAndSet(eid, entityDef);
    }
}
