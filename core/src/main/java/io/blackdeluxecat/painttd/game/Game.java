package io.blackdeluxecat.painttd.game;

import com.artemis.*;
import com.artemis.io.*;
import com.artemis.link.*;
import com.artemis.managers.*;
import com.badlogic.gdx.graphics.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.game.pathfind.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.map.*;
import io.blackdeluxecat.painttd.systems.*;
import io.blackdeluxecat.painttd.systems.render.*;
import io.blackdeluxecat.painttd.systems.request.*;
import io.blackdeluxecat.painttd.systems.utils.*;
import io.blackdeluxecat.painttd.utils.*;

public class Game{
    public static Rule rules;

    //技术性
    public static float lfps = 60f;
    public static World world;
    public static LayerInvocationStrategy lm = new LayerInvocationStrategy();

    public static MapManager map;
    public static GroupManager groups;
    public static WorldSerializationManager worldSerializationManager;
    public static EntityLinkManager entityLinkManager;
    public static StaticUtils utils;

    //每帧开始时重建树, 需自行检查在当前帧中失效的单位.
    public static QuadTree entities = new QuadTree();
    public static FlowField flowField;

    public static CollideQueue collideQueue = new CollideQueue();
    public static DamageQueue damageQueue = new DamageQueue();


    /**初始化Game类及系统*/
    public static void create(){
        createSystems();

        WorldConfigurationBuilder builder = new WorldConfigurationBuilder();
        builder.register(lm);
        lm.lm.layers.forEach(layer -> layer.objects.forEach(builder::with));
        if(world != null) world.dispose();
        world = new World(builder.build());

        worldSerializationManager.setSerializer(new JsonArtemisSerializer(world));

        createLinks();
        Entities.create(world);
    }

    /**创建一张全新的地图*/
    public static void createNewMap(){
        endMap();
        rules = new Rule();
        rules.width = 30;
        rules.height = 20;
        rules.colorPalette = new ColorPalette();
        int l = 6;

        Color color = Vars.c1.set(Color.CYAN);

        for(int i = 0; i < l; i++){
            color.lerp(Color.YELLOW, i / (float)l);
            rules.colorPalette.addColor(color);
        }
        Vars.hud.rebuild();

        endMap();
        map.createMap(rules.width, rules.height);
        flowField = new FlowField(map);
        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                int e = Entities.tileStain.create().getId();
                utils.setPosition(e, x, y);
                map.putEntity(e, "tileStain", x, y);

                int tile = Entities.tile.create().getId();
                utils.setPosition(tile, x, y);
                map.putEntity(tile, "tile", x, y);
            }
        }
        map.shouldRebindTile = true;
    }

    /**移除所有实体, 清空世界状态*/
    public static void endMap(){
        collideQueue.clear();
        damageQueue.clear();

        var oldBag = utils.allEntitiesSub.getEntities();
        for(int i = 0; i < oldBag.size(); i++){
            world.delete(oldBag.get(i));
        }
    }

    public static void dispose(){
        world.dispose();
    }

    /**
     * 持有实体引用的组件, 在此注册{@link com.artemis.annotations.EntityId}字段监听器
     */
    public static void createLinks(){
        entityLinkManager.register(TargetSingleComp.class,
            new LinkAdapter(){
                private ComponentMapper<TargetSingleComp> comp;// relevant fields are injected by default

                @Override
                public void onTargetDead(int sourceId, int deadTargetId){
                    comp.get(sourceId).targetId = -1;
                }
            });
    }

    /**
     * 循环系统层级, z越小, 越早执行
     */
    public static LayerManager.Layer<BaseSystem>
        backend = lm.lm.registerLayer("backend", -10000),
        logicPre = lm.lm.registerLayer("logicFirstWork", 0),
        logicCollide = lm.lm.registerLayer("logicCollide", 100),
        logicShootAI = lm.lm.registerLayer("logicShootAI", 200),
        logic = lm.lm.registerLayer("logic", 300),
        logicPost = lm.lm.registerLayer("logicLastWork", 9000),
        render = lm.lm.registerLayer("render", 10000);

    public static void createSystems(){
        backend.with(l -> {
            l.add(groups = new GroupManager());
            l.add(entityLinkManager = new EntityLinkManager());
            l.add(utils = new StaticUtils());
            l.add(worldSerializationManager = new WorldSerializationManager());
            l.add(map = new MapManager());
        });
        logicPre.with(l -> {
            //l.add(new FlowFieldCoreChangeDetect());
            l.add(new FlowFieldUpdate());
            l.add(new RebuildQuadTree());
        });

        logicCollide.with(l -> {
            l.add(new CollideQueueRemoveNoLongerOverlaps());
            l.add(new CollideDetect());
            l.add(new CollideEnemyRequestDamage());
            l.add(new TileStainRequestDamage());
        });

        logicShootAI.with(l -> {
            l.add(new TargetFind());
            l.add(new CooldownShoot());
            l.add(new ShootSingleRequestDamage());
            l.add(new ShootSingleSlashRequestDamage());
            l.add(new ShootSingleSlashStain());
        });

        logic.with(l -> {
            l.add(new MovementVelGenFlowField());
            l.add(new MovementVelPush());

            l.add(new DamageTypeCollideApply());
            l.add(new DamageTypeDirectApply());
        });

        logicPost.with(l -> {
            l.add(new PostDamageQueue());

            l.add(new MarkHealthDead());
            l.add(new RemoveDead());
        });

        render.with(l -> {
            l.add(new HoverListener());

            l.add(new BaseSystem(){
                @Override
                protected void processSystem(){
                    Core.batch.begin();
                }
            });

            l.add(new DrawPartTexture());

            l.add(new BaseSystem(){
                @Override
                protected void processSystem(){
                    Core.batch.end();
                }
            });

            //使用了ShapeRenderer的系统
            l.add(new DrawFlowFieldDebug());
            l.add(new DrawTileStain());
            l.add(new DrawTile());
            l.add(new DrawUnitHitbox());
            l.add(new DrawTarget());
        });
    }

}
