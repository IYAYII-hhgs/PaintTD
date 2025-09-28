package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class HealthRegenComp extends CopyableComponent{
    public float rate;

    public HealthRegenComp(float rate){
        this.rate = rate;
    }

    public HealthRegenComp(){
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        this.rate = ((HealthRegenComp)other).rate;
        return this;
    }

    @Override
    protected void reset(){
        rate = 0;
    }
}
