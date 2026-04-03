package io.bdc.painttd.content;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.assemble.step.asm.*;

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


    public static EntityDef test;

    public static void load() {
        registerDef(test = new EntityDef("test") {
            {
                steps.add(new TransformStep());
                steps.add(new HitboxStep().setup(1));
            }
        });
    }
}
