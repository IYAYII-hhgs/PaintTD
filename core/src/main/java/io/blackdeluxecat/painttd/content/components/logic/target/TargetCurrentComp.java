package io.blackdeluxecat.painttd.content.components.logic.target;

import io.blackdeluxecat.painttd.content.components.logic.*;

public class TargetCurrentComp extends CopyableComponent{
    public int entityId = -1;

    public TargetCurrentComp(){}

    @Override
    protected void reset(){
        entityId = -1;
    }

    @Override
    public TargetCurrentComp copy(CopyableComponent other){
        TargetCurrentComp tcc = (TargetCurrentComp)other;
        entityId = tcc.entityId;
        return tcc;
    }
}
