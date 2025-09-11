package io.blackdeluxecat.painttd.game;

import com.artemis.*;
import com.artemis.io.*;
import com.artemis.link.*;
import com.artemis.managers.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
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
    public static float lfps = 60f;
    public static World world;
    public static Map map = new Map();
    public static LayerInvocationStrategy lm = new LayerInvocationStrategy();
    public static GroupManager groups;
    public static WorldSerializationManager worldSerializationManager;
    public static EntityLinkManager entityLinkManager;

    /**
     * 每帧开始时重建树, 需自行检查在当前帧中失效的单位.
     */
    public static QuadTree entities = new QuadTree();
    public static FlowField flowField;

    public static CollideQueue collideQueue = new CollideQueue();
    public static DamageQueue damageQueue = new DamageQueue();

    public static StaticUtils utils = new StaticUtils();

    /**初始化Game类及系统*/
    public static void create(){
        createSystems();

        // 创建测试用地图
        setupNewMap();
    }

    /**创建一张全新的地图*/
    public static void setupNewMap(){
        setupWorld();
        setupTileStain();
    }

    /**
     * 初始化World和Map.
     */
    public static void setupWorld(){
        collideQueue.clear();
        damageQueue.clear();

        WorldConfigurationBuilder builder = new WorldConfigurationBuilder();

        builder.register(lm);

        //systems
        groups = new GroupManager();
        builder.with(groups);
        worldSerializationManager = new WorldSerializationManager();
        builder.with(worldSerializationManager);
        entityLinkManager = new EntityLinkManager();
        builder.with(entityLinkManager);
        lm.lm.layers.forEach(layer -> layer.objects.forEach(builder::with));

        if(world != null) world.dispose();
        world = new World(builder.build());

        worldSerializationManager.setSerializer(new JsonArtemisSerializer(world));
        createLinks();

        Entities.create(world);

        ColorPalette palette = new ColorPalette();
        int l = 6;

        Color color = Vars.c1.set(Color.ROYAL);

        for(int i = 0; i < l; i++){
            color.lerp(Color.YELLOW, i / (float)l);
            palette.addColor(color.toIntBits());
        }

        map.create(world, 30, 20, palette);
        Vars.hud.mapEditorTable.buildColorPalette();//重建调色盘ui
        flowField = new FlowField(map);
        flowField.rebuild();
    }

    /**初始化染色瓦片*/
    public static void setupTileStain(){
        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                int e = Entities.tileStain.create().getId();
                utils.setPosition(e, x, y);
                map.putEntity(e, "tileStain", x, y);
            }
        }
    }

    public static void rebindTileStain(){
        var ids = groups.getEntityIds("tileStain");
        for(int i = 0; i < ids.size(); i++){
            PositionComp pos = utils.positionMapper.get(ids.get(i));
            if(map.getTileStain(pos.tileX(), pos.tileY()) != -1){
                Gdx.app.log("ERROR", "TileStain at " + pos.tileX() + ", " + pos.tileY() + " is not -1");
            }
            map.putEntity(ids.get(i), "tileStain", pos.tileX(), pos.tileY());
        }
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
        logicPre = lm.lm.registerLayer("logicFirstWork", 0),
        logicCollide = lm.lm.registerLayer("logicCollide", 100),
        logicShootAI = lm.lm.registerLayer("logicShootAI", 200),
        logic = lm.lm.registerLayer("logic", 300),
        logicPost = lm.lm.registerLayer("logicLastWork", 9000),
        render = lm.lm.registerLayer("render", 10000);

    public static void createSystems(){
        logicPre.with(l -> {
            l.add(utils);
            l.add(new FlowFieldCoreChangeDetect());
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
            l.add(new BaseSystem(){
                @Override
                protected void processSystem(){
                    ScreenUtils.clear(0, 0, 0, 1);
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
            l.add(new DrawColoredTileStain());
            l.add(new DrawMapGrid());
            l.add(new DrawUnitHitbox());
            l.add(new DrawTarget());
        });
    }

}
