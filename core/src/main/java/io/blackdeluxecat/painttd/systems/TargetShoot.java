package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;

public class TargetShoot extends IteratingSystem{

    public TargetShoot(){
        super(Aspect.all(TargetCurrentComp.class));
    }

    @Override
    protected void process(int entityId){

    }
}
