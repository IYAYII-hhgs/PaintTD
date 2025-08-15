package io.blackdeluxecat.painttd.content.components.logic.target;

import io.blackdeluxecat.painttd.content.components.*;

public class TargetComp extends CopyableComponent{
    public int targetId = -1;

    public TargetComp(){}

    @Override
    protected void reset(){
        targetId = -1;
    }

    @Override
    public TargetComp copy(CopyableComponent other){
        TargetComp tcc = (TargetComp)other;
        targetId = tcc.targetId;
        return tcc;
    }
}
