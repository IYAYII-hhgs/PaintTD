package io.blackdeluxecat.painttd.game.content.entitytypes.map;

import com.artemis.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.*;

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
