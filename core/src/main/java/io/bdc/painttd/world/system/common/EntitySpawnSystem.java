package io.bdc.painttd.world.system.common;

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
        for (int i = 0; i < spawnQueue.size(); i++) {
            entityAssembler.spawn(
                    world,
                    spawnQueue.entityDefs.get(i),
                    spawnQueue.eids.get(i),
                    spawnQueue.x.get(i),
                    spawnQueue.y.get(i)
            );
        }
        spawnQueue.clear();
    }
}
