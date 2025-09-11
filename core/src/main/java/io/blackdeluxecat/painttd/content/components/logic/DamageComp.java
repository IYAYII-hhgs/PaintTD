package io.blackdeluxecat.painttd.content.components.logic;

import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.*;

@Transient
public class DamageComp extends CopyableComponent{
    public float damage;

    public DamageComp(){
    }

    public DamageComp(float damage){
        this.damage = damage;
    }

    @Override
    public DamageComp copy(CopyableComponent other){
        DamageComp damageComp = (DamageComp)other;
        this.damage = damageComp.damage;
        return this;
    }

    @Override
    protected void reset(){
    }

    @Override
    public void refill(CopyableComponent def){
        copy(def);
    }
}
