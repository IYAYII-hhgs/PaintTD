package io.bdc.painttd.world;

import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.family.bullet.*;
import io.bdc.painttd.world.family.targeting.*;
import io.bdc.painttd.world.family.turret.*;
import io.bdc.painttd.world.family.weapon.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;
import io.bdc.painttd.world.system.common.*;
import io.bdc.painttd.world.system.render.*;
import io.bdc.painttd.world.system.targeting.*;

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
        world.addStore(new TileBucketDirtyQueue());
        world.addStore(new TileBucketDebugStore());
        world.addStore(new CollisionDebugStore());
        world.addStore(new CollisionSolidBounceStore());
        world.addStore(new PositionStore());
        world.addStore(new VelocityStore());
        world.addStore(new HitboxStore());
        world.addStore(new CollisionBodyStore());
        world.addStore(new EntityMetaStore());

        world.addStore(new EntityHealthStore());
        world.addStore(new EntityTeamStore());

        world.addStore(new TurretStore());
        world.addStore(new BulletWeaponStore());
        world.addStore(new BulletStore());
        world.addStore(new EntityTargetingStore());
        world.addStore(new CellTargetingStore());
    }

    public void createAPIs(WorldRuntime world) {
        world.addApi(new DamageAPI());
        world.addApi(new TransformAPI());
        world.addApi(new TileBucketQueryAPI());
        world.addApi(new CollisionDispatchAPI());
        world.addApi(new MapAPI());
        world.addApi(new ActTypeAPI());
    }

    public void createSystems(WorldRuntime world) {
        var entityAssembler = world.entityAssembler;
        entityAssembler.bindStore(world.access);
        // 实体生命周期
        world.addSystem(new EntitySpawnSystem(world, WorldPhase.SPAWN, 0, entityAssembler));

        world.addSystem(new VelocityApplySystem(world, WorldPhase.SIMULATE, 10));
        world.addSystem(new VelocityDecaySystem(world, WorldPhase.SIMULATE, 20));
        world.addSystem(new BulletTargetPushSystem(world, WorldPhase.SIMULATE, 25));
        world.addSystem(new EntityTargetingSystem(world, WorldPhase.SIMULATE, 50));
        world.addSystem(new CellTargetingSystem(world, WorldPhase.SIMULATE, 55));
        world.addSystem(new CooldownSystem(world, WorldPhase.SIMULATE, 60));
        world.addSystem(new BulletWeaponAttackSystem(world, WorldPhase.SIMULATE, 70));
        world.addSystem(new TileBucketSyncSystem(world, WorldPhase.SIMULATE, 100));
        world.addSystem(new CollisionFrameBeginSystem(world, WorldPhase.SIMULATE, 110));
        world.addSystem(new CollisionSoftRepulsionSystem(world, WorldPhase.SIMULATE, 115));
        world.addSystem(new DetectEeAabbSystem(world, WorldPhase.SIMULATE, 120));
        world.addSystem(new DetectEcSolidCellSystem(world, WorldPhase.SIMULATE, 130));
        world.addSystem(new CollisionSolidBounceSystem(world, WorldPhase.SIMULATE, 140));
        world.addSystem(new WorldBoundsBounceSystem(world, WorldPhase.SIMULATE, 150));

        world.addSystem(new CollisionBulletHitSystem(world, WorldPhase.APPLY, 100));
        world.addSystem(new CollisionCellDamageSystem(world, WorldPhase.APPLY, 110));

        world.addSystem(new BulletValidationSystem(world, WorldPhase.CLEANUP, 80));
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
