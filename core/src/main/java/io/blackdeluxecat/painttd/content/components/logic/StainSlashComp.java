package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class StainSlashComp extends CopyableComponent{
    public int range;
    public float damage = 1;

    public StainSlashComp(){
    }

    public StainSlashComp(int range){
        this.range = range;
    }

    @Override
    public StainSlashComp copy(CopyableComponent other){
        StainSlashComp otherComp = (StainSlashComp)other;
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
