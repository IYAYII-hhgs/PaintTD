package io.blackdeluxecat.painttd.game.content;

import io.blackdeluxecat.painttd.game.content.components.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.unit.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.building.*;

public class EntityTypes{
    public static BaseEntityType
        //enemies
        debug,
        //turrets
        debugTurret;

    public static void create(){
        debug = new DebugEnemy();
        debugTurret = new BaseTurret("debug"){
            {
                def.add(new DamageComp(1));
                def.add(new RangeComp(5));
            }
        };
    }
}
