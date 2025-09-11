package io.blackdeluxecat.painttd.content.components.logic;

import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.*;

@Transient
public class ArmorComp extends CopyableComponent{
    public float armor;

    public ArmorComp(){
    }

    public ArmorComp(float armor){
        this.armor = armor;
    }

    @Override
    public ArmorComp copy(CopyableComponent other){
        ArmorComp armorComp = (ArmorComp)other;
        armor = armorComp.armor;
        return this;
    }

    @Override
    protected void reset(){
        armor = 0;
    }

    @Override
    public void refill(CopyableComponent def){
        copy(def);
    }
}
