package io.blackdeluxecat.painttd.game.content;

import io.blackdeluxecat.painttd.game.content.components.logic.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.building.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.unit.*;

public class EntityTypes{
    public static BaseEntityType
        //enemies
        debug,
        //turrets
        debugTurret;

    public static void create(){
        debug = new Enemy();
        debugTurret = new BaseTurret("debug"){
            {
                def.add(new DamageComp(1));
                def.add(new RangeComp(5));
            }
        };
    }
}
