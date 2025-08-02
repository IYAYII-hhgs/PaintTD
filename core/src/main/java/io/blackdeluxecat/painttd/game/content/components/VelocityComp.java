package io.blackdeluxecat.painttd.game.content.components;

public class VelocityComp extends CloneableComponent{
    public float x, y;
    public VelocityComp(){}

    public VelocityComp(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    protected void reset(){
        x = 0;
        y = 0;
    }

    @Override
    public VelocityComp copy(){
        return new VelocityComp(x, y);
    }
}
