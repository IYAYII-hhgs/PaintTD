package io.blackdeluxecat.painttd.game.content.entitytypes.unit;

import com.artemis.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.game.content.components.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.*;

public class DebugEnemy extends BaseEntityType{
    public DebugEnemy(){
        super("debug");
        def.add(new PositionComp());
    }

    @Override
    public Entity create(){
        Entity e = super.create();
        Game.groups.add(e, "Unit");
        return e;
    }
}
