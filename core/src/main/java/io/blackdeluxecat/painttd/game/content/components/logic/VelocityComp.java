package io.blackdeluxecat.painttd.game.content.components.logic;

public class VelocityComp extends CopyableComponent{
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
    public VelocityComp copy(CopyableComponent other){
        VelocityComp velocityComp = (VelocityComp) other;
        velocityComp.x = x;
        velocityComp.y = y;
        return velocityComp;
    }
}
