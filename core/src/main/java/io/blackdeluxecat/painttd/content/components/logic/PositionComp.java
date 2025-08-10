package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class PositionComp extends CopyableComponent{
    public float x, y;

    public PositionComp(){}

    public PositionComp(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    protected void reset(){
        x = 0;
        y = 0;
    }

    @Override
    public PositionComp copy(CopyableComponent other){
        PositionComp positionComp = (PositionComp)other;
        this.x = positionComp.x;
        this.y = positionComp.y;
        return this;
    }
}
