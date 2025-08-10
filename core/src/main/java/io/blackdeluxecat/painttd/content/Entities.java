package io.blackdeluxecat.painttd.content;

import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.movement.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;

public class Entities{
    public static BaseEntityType
        //enemies
        eraser,
        //turrets
        pencil;

    public static void create(){
        eraser = new BaseEntityType("eraser"){
            {
                def.add(new PositionComp());
                def.add(new HealthComp(1));
                def.add(new ArmorComp(0));
                def.add(new SizeComp(1));
                def.add(new MoveSpeedComp(1));
                def.add(new VelocityComp());
            }
        };

        pencil = new BaseEntityType("pencil"){
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
