package io.bdc.painttd.world.system.common;

import io.bdc.painttd.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class EntitySpawnSystem extends WorldSystem {
    public SpawnRequestQueue spawnQueue;
    public EntityAssembler entityAssembler;

    public EntitySpawnSystem(WorldRuntime world, WorldPhase phase, int order, EntityAssembler entityAssembler) {
        super(world, phase, order);
        this.entityAssembler = entityAssembler;
    }

    @Override
    public void onStoreBind(WorldStoreBinder binder) {
        spawnQueue = binder.require(SpawnRequestQueue.class);
    }

    @Override
    public void run(float delta) {
        try {
            for (int i = 0; i < spawnQueue.size(); i++) {
                var request = spawnQueue.requests.get(i);
                int eid = world.idManager.alloc();
                try {
                    entityAssembler.assemble(eid, request);
                } catch (RuntimeException exception) {
                    String entityKind = request != null
                            && request.entityDef != null
                            ? request.entityDef.name
                            : "<missing-def>";
                    String message = "Fatal Spawn failed for eid=" + eid + ", kind=" + entityKind + ".";
                    PaintTD.log.error(message, exception);
                    throw new IllegalStateException(message, exception);
                }
            }
        } finally {
            spawnQueue.clear();
        }
    }
}
