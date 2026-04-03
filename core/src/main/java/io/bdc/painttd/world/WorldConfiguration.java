package io.bdc.painttd.world;

import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.common.*;
import io.bdc.painttd.world.system.render.*;

public class WorldConfiguration {
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
        var entityAssembler = world.entityAssembler;
        entityAssembler.bindStore(world.storeBinder);
        // 实体生命周期
        world.addSystem(new EntitySpawnSystem(world, WorldPhase.SPAWN, 0, entityAssembler));
        world.addSystem(new EntityDestroySystem(world, WorldPhase.CLEANUP, 0, entityAssembler));

        world.addSystem(new PreRenderSystem(world, WorldPhase.RENDER_PREPARE, 0));
        // 图形渲染
        world.addSystem(new DrawHitboxSystem(world, WorldPhase.RENDER_ENTITY, 0));

        world.addSystem(new PostRenderSystem(world, WorldPhase.RENDER_POST, 0));
    }

}
