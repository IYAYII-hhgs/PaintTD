package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class StainSplashComp extends CopyableComponent{
    public int range;
    public float damage = 1;

    public StainSplashComp(){
    }

    public StainSplashComp(int range){
        this.range = range;
    }

    @Override
    public StainSplashComp copy(CopyableComponent other){
        StainSplashComp otherComp = (StainSplashComp)other;
        this.range = otherComp.range;
        this.damage = otherComp.damage;
        return this;
    }

    @Override
    protected void reset(){
        this.range = 0;
        this.damage = 1;
    }
}
