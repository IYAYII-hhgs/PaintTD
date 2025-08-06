package io.blackdeluxecat.painttd.game.content.entitytypes.unit;

import com.artemis.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.*;

public class BaseUnit extends BaseEntityType{
    public BaseUnit(String name){
        super(name);
    }

    @Override
    public Entity create(){
        Entity e = super.create();
        Game.groups.add(e, "BaseUnit");
        return e;
    }
}
