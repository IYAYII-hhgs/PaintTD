package io.blackdeluxecat.painttd.game.content.components;

import com.artemis.*;

public class RangeComp extends CloneableComponent{
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
    public CloneableComponent copy(){
        return new RangeComp(range);
    }
}
