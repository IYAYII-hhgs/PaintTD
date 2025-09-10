package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;

@IsLogicProcess
@Wire
public class EnergyRegenerate extends IteratingSystem{
    public ComponentMapper<EnergyComp> em;
    public ComponentMapper<EnergyRegenComp> erm;

    public EnergyRegenerate(){
        super(Aspect.all(EnergyComp.class, EnergyRegenComp.class));
    }

    @Override
    protected void process(int entityId){
        em.get(entityId).energy += erm.get(entityId).regenRate;
    }
}
