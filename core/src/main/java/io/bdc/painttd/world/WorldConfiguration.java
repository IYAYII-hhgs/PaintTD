package io.bdc.painttd.world;

import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.common.*;
import io.bdc.painttd.world.system.render.*;

public class WorldConfiguration {
    public int mapWidth, mapHeight;

    public WorldConfiguration setupMap(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        return this;
    }

    /** 装配标准世界 */
    public void assemble(WorldRuntime world) {
        createStores(world);
        createAPIs(world);
        createSystems(world);
        world.sortSystems();
    }

    public void createStores(WorldRuntime world) {
        world.addStore(new SpawnRequestQueue());
        world.addStore(new DestroyQueue());

        world.addStore(new MapStore(mapWidth, mapHeight));
        world.addStore(new TileBucketStore(mapWidth, mapHeight, 40000));
        world.addStore(new TileBucketDebugStore());
        world.addStore(new TransformStore());
        world.addStore(new HitboxStore());
        world.addStore(new EntityMetaStore());

        world.addStore(new EntityHealthStore());
        world.addStore(new EntityTeamStore());
    }

    public void createAPIs(WorldRuntime world) {
        world.addApi(new DamageAPI());
        world.addApi(new TileBucketQueryAPI());
    }

    public void createSystems(WorldRuntime world) {
        var entityAssembler = world.entityAssembler;
        entityAssembler.bindStore(world.access);
        // 实体生命周期
        world.addSystem(new EntitySpawnSystem(world, WorldPhase.SPAWN, 0, entityAssembler));

        world.addSystem(new TileBucketSyncSystem(world, WorldPhase.SIMULATE, 100));

        //world.addSystem(new TestGlobalDamageSystem(world, WorldPhase.APPLY, 0));

        world.addSystem(new DeathToDestroySystem(world, WorldPhase.CLEANUP, 90));
        world.addSystem(new EntityDestroySystem(world, WorldPhase.CLEANUP, 100, entityAssembler));

        world.addSystem(new PreRenderSystem(world, WorldPhase.RENDER_PREPARE, 0));
        // 图形渲染
        world.addSystem(new DrawMapSystem(world, WorldPhase.RENDER_TERRAIN, 0));
        world.addSystem(new DrawHitboxSystem(world, WorldPhase.RENDER_ENTITY, 0));
        world.addSystem(new DrawTileBucketDebugSystem(world, WorldPhase.RENDER_DEBUG, 0));

        world.addSystem(new PostRenderSystem(world, WorldPhase.RENDER_POST, 0));
    }

}
