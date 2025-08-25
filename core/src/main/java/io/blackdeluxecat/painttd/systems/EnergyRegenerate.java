package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;

import static io.blackdeluxecat.painttd.game.Game.*;

public class EnergyRegenerate extends IteratingSystem{
    public ComponentMapper<EnergyComp> em;
    public ComponentMapper<EnergyRegenComp> erm;

    public EnergyRegenerate(){
        super(Aspect.all(EnergyComp.class, EnergyRegenComp.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        em = world.getMapper(EnergyComp.class);
        erm = world.getMapper(EnergyRegenComp.class);
    }

    @Override
    protected void process(int entityId){
        em.get(entityId).energy += erm.get(entityId).regenRate;
    }
}
