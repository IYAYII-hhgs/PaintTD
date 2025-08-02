package io.blackdeluxecat.painttd.game.content.components;

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
    public RangeComp copy(){
        return new RangeComp(range);
    }
}
