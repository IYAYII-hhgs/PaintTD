package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.request.*;
import io.bdc.painttd.world.store.*;

public final class EntityMetaStep implements AssembleStep {
    public static final String TYPE = "EntityMetaStep";

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldStoreBinder binder) {
        EntityDef entityDef = req.entityDef;
        if (entityDef == null) {
            throw new IllegalStateException("EntitySpawnRequest.entityDef is required.");
        }
        binder.require(EntityMetaStore.class).put(eid, entityDef);
    }
}
