package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

/**能量系统回复组件。*/
public class EnergyRegenComp extends CopyableComponent{
    public float regenRate;
    public EnergyRegenComp(){}

    public EnergyRegenComp(float regenPreSec){
        this.regenRate = regenPreSec;
    }

    @Override
    public EnergyRegenComp copy(CopyableComponent other){
        EnergyRegenComp energyRegenComp = (EnergyRegenComp)other;
        energyRegenComp.regenRate = regenRate;
        return energyRegenComp;
    }

    @Override
    protected void reset(){
        regenRate = 0;
    }
}
