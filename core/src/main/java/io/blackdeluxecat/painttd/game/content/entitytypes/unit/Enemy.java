package io.blackdeluxecat.painttd.game.content.entitytypes.unit;

import com.artemis.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.game.content.components.logic.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.*;

public class Enemy extends BaseEntityType{
    public Enemy(){
        super("debug");
        def.add(new PositionComp());
        def.add(new HealthComp(1));
        def.add(new ArmorComp(0));
        def.add(new SizeComp(1));
        def.add(new MoveSpeedComp(1));
    }

    @Override
    public Entity create(){
        Entity e = super.create();
        Game.groups.add(e, "Unit");
        return e;
    }
}
