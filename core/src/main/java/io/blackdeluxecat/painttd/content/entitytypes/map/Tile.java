package io.blackdeluxecat.painttd.content.entitytypes.map;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.entitytypes.*;
import io.blackdeluxecat.painttd.game.*;

public class Tile extends BaseEntityType{
    public Tile(String name) {
        super(name);
    }

    @Override
    public Entity create(){
        Entity e = super.create();
        Game.groups.add(e, "Map");
        Game.groups.add(e, "Tile");
        return e;
    }
}
