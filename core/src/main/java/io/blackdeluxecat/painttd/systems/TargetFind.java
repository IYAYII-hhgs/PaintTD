package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;

public class TargetFind extends IteratingSystem{

    public TargetFind(){
        super(Aspect.all(TargetCurrentComp.class, TargetPriorityComp.class));
    }

    @Override
    protected void process(int entityId){
        TargetCurrentComp current = world.getMapper(TargetCurrentComp.class).get(entityId);
        TargetPriorityComp priority = world.getMapper(TargetPriorityComp.class).get(entityId);
    }
}
