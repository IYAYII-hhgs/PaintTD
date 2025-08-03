package io.blackdeluxecat.painttd.game.content.entitytypes.building;

import com.artemis.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.game.content.components.logic.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.*;

public class BaseTurret extends BaseEntityType{
    public BaseTurret(String name){
        super(name);
        def.add(new PositionComp());
        def.add(new HealthComp(1));
        def.add(new SizeComp(1));
        def.add(new EnergyComp(2));
        def.add(new RangeComp(6));
        def.add(new CooldownComp(1));
        def.add(new DamageComp(1));
    }

    @Override
    public Entity create(){
        Entity e = super.create();
        Game.groups.add(e, "Building");
        Game.groups.add(e, "Turret");
        return e;
    }
}