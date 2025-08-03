package io.blackdeluxecat.painttd.game.content.components.logic;

/**能量系统回复组件。*/
public class EnergyRegenComp extends CopyableComponent{
    public float regenRate;
    public EnergyRegenComp(){}

    public EnergyRegenComp(float regenRate){
        this.regenRate = regenRate;
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
