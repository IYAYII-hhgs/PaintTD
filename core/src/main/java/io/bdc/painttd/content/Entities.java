package io.bdc.painttd.content;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.assemble.step.asm.*;
import io.bdc.painttd.world.store.*;

/**
 * 全局静态实体定义表
 */

public class Entities {
    private static int lastId = -1;
    public static int nextId() {
        return ++lastId;
    }
    public static Array<EntityDef> allEntityDefs = new Array<>();
    public static void registerDef(EntityDef def) {
        allEntityDefs.add(def);
        def.id = nextId();
    }

    public static EntityDef test, testBuilding;

    public static void load() {
        registerDef(test = new EntityDef("test") {
            {
                steps.add(new TransformStep());
                steps.add(new HitboxStep().setup(0.8f));
                steps.add(new VelocityStep());
                steps.add(new CollisionBodyStep().setup(CollisionBodyStore.BODY_DYNAMIC));
                steps.add(new EntityHealthStep().setup(1, 1));
                steps.add(new DebugTeamStep().setup(1));
            }
        });

        registerDef(testBuilding = new EntityDef("testBuilding") {
            {
                steps.add(new BuildingTransformStep());
                steps.add(new HitboxStep().setup(1));
                steps.add(new VelocityStep());
                steps.add(new CollisionBodyStep().setup(CollisionBodyStore.BODY_STATIC));
                steps.add(new EntityHealthStep().setup(4, 4));
                steps.add(new DebugTeamStep().setup(0));
            }
        });
    }
}
