package io.blackdeluxecat.painttd.game.content;

import io.blackdeluxecat.painttd.game.content.entitytypes.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.enemy.*;

public class EntityTypes{
    public static BaseEntityType debug;

    public static void create(){
        debug = new DebugEnemy();
    }
}
