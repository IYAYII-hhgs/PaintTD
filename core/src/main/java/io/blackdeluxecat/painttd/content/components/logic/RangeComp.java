package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class RangeComp extends CopyableComponent{
    public int range;

    public RangeComp(){}

    public RangeComp(int range){
        this.range = range;
    }

    @Override
    protected void reset(){
        range = 0;
    }

    @Override
    public RangeComp copy(CopyableComponent other){
        RangeComp rangeComp = (RangeComp) other;
        rangeComp.range = range;
        return rangeComp;
    }
}
