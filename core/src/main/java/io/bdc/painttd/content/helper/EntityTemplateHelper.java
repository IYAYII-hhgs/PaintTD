package io.bdc.painttd.content.helper;

import io.bdc.painttd.content.def.EntityDef;
import io.bdc.painttd.content.def.WorldDef;

public final class EntityTemplateHelper {
    private EntityTemplateHelper() {
    }

    public static EntityDef entity(int id, String kind) {
        EntityDef def = new EntityDef(id);
        def.kind = kind;
        return def;
    }

    public static WorldDef world(int id) {
        return new WorldDef(id);
    }
}
