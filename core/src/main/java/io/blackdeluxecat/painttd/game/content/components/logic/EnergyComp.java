package io.blackdeluxecat.painttd.game.content.components.logic;


/**能量系统的储量组件。*/
public class EnergyComp extends CopyableComponent{
    public float energy, maxEnergy;

    public EnergyComp(){}

    public EnergyComp(float maxEnergy){
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    @Override
    public EnergyComp copy(CopyableComponent other){
        EnergyComp energyComp = (EnergyComp)other;
        energyComp.energy = energy;
        energyComp.maxEnergy = maxEnergy;
        return energyComp;
    }

    @Override
    protected void reset(){
        energy = 0;
        maxEnergy = 0;
    }
}
