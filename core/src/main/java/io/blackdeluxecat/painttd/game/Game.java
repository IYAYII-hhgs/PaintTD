package io.blackdeluxecat.painttd.game;

import com.artemis.*;
import com.artemis.managers.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.game.content.*;
import io.blackdeluxecat.painttd.game.systems.*;

public class Game{
    public static World world;
    public static LayerInvocationStrategy lm = new LayerInvocationStrategy();
    public static GroupManager groups = new GroupManager();

    public static void create(){
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder();

        builder.register(lm);
        createSystems();

        //systems
        lm.layers.sort((l1, l2) -> Float.compare(l1.z, l2.z));
        lm.layers.forEach(layer -> {
            layer.systems.forEach(builder::with);
        });
        builder.with(groups);

        world = new World(builder.build());

        EntityTypes.create();
    }

    public static void createSystems(){
        logic.with(l -> {
            l.add(new DebugRandomMover());
        });

        render.with(l -> {
            l.add(new DebugDrawer());
            l.add(new BaseSystem(){
                @Override
                protected void processSystem(){
                    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
                }
            });
        });
    }

    public static final float LOGIC_LAYER = 0, RENDER_LAYER = 10000;

    public static LayerInvocationStrategy.Layer
        render = lm.registerLayer("render", RENDER_LAYER),
        logic = lm.registerLayer("logic", LOGIC_LAYER);
}
