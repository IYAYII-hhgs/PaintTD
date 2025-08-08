package io.blackdeluxecat.painttd.content.entitytypes.unit;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.entitytypes.*;
import io.blackdeluxecat.painttd.game.*;

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
