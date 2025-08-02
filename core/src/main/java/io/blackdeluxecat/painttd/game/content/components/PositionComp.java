package io.blackdeluxecat.painttd.game.content.components;

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
    public PositionComp copy(){
        return new PositionComp(x, y);
    }
}
