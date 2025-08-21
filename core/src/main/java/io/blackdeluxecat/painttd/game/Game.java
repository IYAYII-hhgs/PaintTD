package io.blackdeluxecat.painttd.game;

import com.artemis.*;
import com.artemis.managers.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.map.Map;
import io.blackdeluxecat.painttd.struct.*;
import io.blackdeluxecat.painttd.systems.*;

public class Game{
    public static float lfps = 60;
    public static World world;
    public static Map map = new Map();
    public static LayerInvocationStrategy lm = new LayerInvocationStrategy();
    public static GroupManager groups = new GroupManager();

    /**每帧开始时重建树, 已失效的单位不会被加入, 在当前帧中失效的单位直到帧结束才会被移除.*/
    public static QuadTree entities = new QuadTree();

    public static void create(){
        createSystems();
        loadMap();
    }

    /**重建整个世界, 加载地图.*/
    public static void loadMap(){
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder();

        builder.register(lm);

        //systems
        lm.lm.layers.forEach(layer -> layer.objects.forEach(builder::with));
        builder.with(groups);

        if(world != null) world.dispose();
        world = new World(builder.build());

        //为单位创建默认组件。def中的组件来自new构造，没有进入池化管理，copy到world中的过程也只是属性拷贝，不涉及池化管理。
        Entities.create();
        map.create(world, 30, 20);
    }

    public static final float LOGIC_LAYER = 0, RENDER_LAYER = 10000;

    public static LayerManager.Layer<BaseSystem>
        render = lm.lm.registerLayer("render", RENDER_LAYER),
        logic = lm.lm.registerLayer("logic", LOGIC_LAYER);

    public static void createSystems(){
        logic.with(l -> {
            l.add(new RebuildQuadTree());
            l.add(new CollideDetect());

            l.add(new MovementVelocity());
            l.add(new TargetFind());
            l.add(new CooldownShoot());

            l.add(new EnergyRegenerate());
            l.add(new DamageDeal());
            l.add(new MarkHealthDead());
            l.add(new RemoveDead());
        });

        render.with(l -> {
            l.add(new BaseSystem(){
                @Override
                protected void processSystem(){
                    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
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
            l.add(new DrawMapGrid());
            l.add(new DrawDebugHitbox());
            l.add(new DrawTarget());

        });
    }

}
