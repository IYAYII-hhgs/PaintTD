package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class StainSlashComp extends CopyableComponent{
    public int range;

    public StainSlashComp(){
    }

    public StainSlashComp(int range){
        this.range = range;
    }

    @Override
    public StainSlashComp copy(CopyableComponent other){
        StainSlashComp otherComp = (StainSlashComp)other;
        this.range = otherComp.range;
        return this;
    }

    @Override
    protected void reset(){

    }
}
