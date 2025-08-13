package io.blackdeluxecat.painttd.content.components.logic.target;

import io.blackdeluxecat.painttd.content.components.*;

public class TargetCurrentComp extends CopyableComponent{
    public int targetId = -1;

    public TargetCurrentComp(){}

    @Override
    protected void reset(){
        targetId = -1;
    }

    @Override
    public TargetCurrentComp copy(CopyableComponent other){
        TargetCurrentComp tcc = (TargetCurrentComp)other;
        targetId = tcc.targetId;
        return tcc;
    }
}
