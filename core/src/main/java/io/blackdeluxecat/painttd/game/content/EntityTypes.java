package io.blackdeluxecat.painttd.game.content;

import io.blackdeluxecat.painttd.game.content.components.logic.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.building.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.unit.*;

public class EntityTypes{
    public static BaseEntityType
        //enemies
        eraser,
        //turrets
        pencil;

    public static void create(){
        eraser = new BaseUnit("eraser"){
            {
                def.add(new PositionComp());
                def.add(new HealthComp(1));
                def.add(new ArmorComp(0));
                def.add(new SizeComp(1));
                def.add(new MoveSpeedComp(1));
                def.add(new VelocityComp());
            }
        };

        pencil = new BaseTurret("pencil"){
            {
                def.add(new PositionComp());
                def.add(new HealthComp(1));
                def.add(new SizeComp(1));
                def.add(new EnergyComp(2));
                def.add(new EnergyRegenComp(2));
                def.add(new RangeComp(8));
                def.add(new CooldownComp(1));
                def.add(new DamageComp(1));
                def.add(new TargetPriorityComp(TargetPriorityComp.CLOSEST, TargetPriorityComp.SORT_DESCENDING));
            }
        };
    }
}
