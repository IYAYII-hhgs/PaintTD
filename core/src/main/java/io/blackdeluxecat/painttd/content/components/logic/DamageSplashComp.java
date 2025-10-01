package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class DamageSplashComp extends CopyableComponent{
    public float damage;
    public float range;

    public DamageSplashComp(){
    }

    public DamageSplashComp(float damage, float range){
        this.damage = damage;
        this.range = range;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        DamageSplashComp otherComp = (DamageSplashComp)other;
        this.damage = otherComp.damage;
        this.range = otherComp.range;
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
