package io.blackdeluxecat.painttd.game;

import com.artemis.*;
import com.artemis.managers.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.game.content.*;
import io.blackdeluxecat.painttd.game.systems.*;
import io.blackdeluxecat.painttd.lib.struct.*;

public class Game{
    public static World world;
    public static LayerInvocationStrategy lm = new LayerInvocationStrategy();
    public static GroupManager groups = new GroupManager();

    public static void createSystems(){
        logic.with(l -> {
            l.add(new DebugRandomMover());
        });

        render.with(l -> {
            l.add(new BaseSystem(){
                @Override
                protected void processSystem(){
                    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
                }
            });
            l.add(new DebugDrawer());
        });
    }

    public static void create(){
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder();

        builder.register(lm);
        createSystems();

        //systems
        lm.lm.layers.forEach(layer -> layer.objects.forEach(builder::with));
        builder.with(groups);

        if(world != null) world.dispose();  //以防有人替换world
        world = new World(builder.build());

        //为单位创建默认组件。尽管组件并不指向world，所以是否有必要在world每次重建时创建一遍？是有必要的，这里的一切组件都在world.cm中进行池化，不能让任何组件脱离池化管理。等下，因为def中的组件来自new构造，因此没有进入池化管理，copy到world中的过程也只是重载的自定义属性拷贝，不涉及池化管理。
        EntityTypes.create();
    }

    public static final float LOGIC_LAYER = 0, RENDER_LAYER = 10000;

    public static LayerManager.Layer<BaseSystem>
        render = lm.lm.registerLayer("render", RENDER_LAYER),
        logic = lm.lm.registerLayer("logic", LOGIC_LAYER);
}
