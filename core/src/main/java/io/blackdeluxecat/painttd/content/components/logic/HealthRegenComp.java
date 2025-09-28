package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class HealthRegenComp extends CopyableComponent{
    public float regenRate;

    public HealthRegenComp(float regenRate){
        this.regenRate = regenRate;
    }

    public HealthRegenComp(){
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        this.regenRate = ((HealthRegenComp)other).regenRate;
        return this;
    }

    @Override
    protected void reset(){
        regenRate = 0;
    }
}
