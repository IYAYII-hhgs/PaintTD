package io.blackdeluxecat.painttd.game.content.entitytypes.building;

import com.artemis.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.game.content.components.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.*;

public class BaseTurret extends BaseEntityType{
    public BaseTurret(String name){
        super(name);
        def.add(new PositionComp());
    }

    @Override
    public Entity create(){
        Entity e = super.create();
        Game.groups.add(e, "Turret");
        return e;
    }
}