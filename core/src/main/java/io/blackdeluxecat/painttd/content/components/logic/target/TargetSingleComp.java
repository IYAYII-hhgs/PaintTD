package io.blackdeluxecat.painttd.content.components.logic.target;

import io.blackdeluxecat.painttd.content.components.*;

public class TargetSingleComp extends CopyableComponent{
    public int targetId = -1;

    public TargetSingleComp(){}

    @Override
    protected void reset(){
        targetId = -1;
    }

    @Override
    public TargetSingleComp copy(CopyableComponent other){
        TargetSingleComp tcc = (TargetSingleComp)other;
        targetId = tcc.targetId;
        return tcc;
    }
}
