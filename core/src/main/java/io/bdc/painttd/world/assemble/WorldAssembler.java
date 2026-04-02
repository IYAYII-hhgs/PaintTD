package io.bdc.painttd.world.assemble;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.common.*;

public class WorldAssembler {
    /** 装配标准世界 */
    public void assemble(WorldRuntime world) {
        createStores(world);
        createSystems(world);
        world.sortSystems();
    }

    public void createStores(WorldRuntime world) {
        world.addStore(new SpawnRequestQueue());
        world.addStore(new DestroyRequestQueue());

        world.addStore(new MapStore());
        world.addStore(new TransformStore());
        world.addStore(new HitboxStore());
        world.addStore(new EntityMetaStore());
    }

    public void createSystems(WorldRuntime world) {
        var entityAssembler = new EntityAssembler();
        entityAssembler.onStoreBind(world.storeBinder);
        world.addSystem(new EntitySpawnSystem(world, WorldPhase.SPAWN, 0, entityAssembler));
        world.addSystem(new EntityDestroySystem(world, WorldPhase.CLEANUP, 0, entityAssembler));
    }

}
