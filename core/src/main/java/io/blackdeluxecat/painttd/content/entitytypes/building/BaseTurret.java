package io.blackdeluxecat.painttd.content.entitytypes.building;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.entitytypes.*;
import io.blackdeluxecat.painttd.game.*;

public class BaseTurret extends BaseEntityType{
    public BaseTurret(String name){
        super(name);
    }

    @Override
    public Entity create(){
        Entity e = super.create();
        Game.groups.add(e, "Building");
        Game.groups.add(e, "BaseTurret");
        return e;
    }
}