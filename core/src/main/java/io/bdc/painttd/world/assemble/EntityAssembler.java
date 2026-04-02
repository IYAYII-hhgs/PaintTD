package io.bdc.painttd.world.assemble;

import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.assemble.step.asm.*;
import io.bdc.painttd.world.store.*;

public class EntityAssembler {
    public WorldStoreBinder binder;
    private final EntityMetaStep metaStep = new EntityMetaStep();

    public void onStoreBind(WorldStoreBinder binder) {
        this.binder = binder;
    }

    public void assemble(int eid, EntitySpawnRequest req) {
        WorldStoreBinder activeBinder = requireBinder();
        if (req == null) {
            throw new IllegalArgumentException("EntitySpawnRequest is required.");
        }
        EntityDef entityDef = req.entityDef;
        if (entityDef == null) {
            throw new IllegalStateException("EntitySpawnRequest.entityDef is required.");
        }

        metaStep.run(eid, req, activeBinder);

        for (AssembleStep step : req.entityDef.steps) {
            step.run(eid, req, activeBinder);
        }
        for (PostSpawnStep step : req.extraSteps) {
            step.run(eid, req, activeBinder);
        }
    }

    /**
     * Removes this entity from the stores owned by the assembler.
     * <p>
     * Missing entity ids are treated as a no-op so recycle callers can forward queued destroy
     * requests without checking store membership first.
     */
    public void destroy(WorldRuntime runtime, int eid) {
        for (var store : runtime.stores) {
            if (store.value instanceof EntityOwner owner) {
                owner.onEntityDestroy(eid);
            }
        }
    }

    private WorldStoreBinder requireBinder() {
        if (binder == null) {
            throw new IllegalStateException("EntityAssembler is not bound to stores.");
        }
        return binder;
    }
}
