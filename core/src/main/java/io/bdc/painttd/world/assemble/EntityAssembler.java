package io.bdc.painttd.world.assemble;

import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

public class EntityAssembler {
    public void spawn(WorldRuntime runtime, EntityDef entityDef, int eid, float x, float y) {
        runtime.getStore(EntityMetaStore.class).put(eid, entityDef);
        runtime.getStore(TransformStore.class).put(eid, x, y);
    }

    public void destroy(WorldRuntime runtime, int eid) {
        runtime.getStore(TransformStore.class).remove(eid);
        runtime.getStore(EntityMetaStore.class).remove(eid);
    }
}
