package io.blackdeluxecat.painttd.game.content.components.logic;

public class ArmorComp extends CopyableComponent{
    public float armor;

    public ArmorComp(){}

    public ArmorComp(float armor){
        this.armor = armor;
    }

    @Override
    public ArmorComp copy(CopyableComponent other){
        ArmorComp armorComp = (ArmorComp)other;
        armorComp.armor = armor;
        return armorComp;
    }

    @Override
    protected void reset(){
        armor = 0;
    }
}
