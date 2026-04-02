package io.bdc.painttd.content.def;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.assemble.step.*;

public class EntityDef {
    public String name;

    /** 已编译的steps序列, 高效装配实体 */
    public final Array<AssembleStep> steps = new Array<>();

    public ObjectMap<String, Object> properties = new ObjectMap<>();

    /** 运行时分配id */
    public int id;

    public EntityDef(String name) {
        this.name = name;
    }
}
