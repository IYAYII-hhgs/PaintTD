package io.blackdeluxecat.painttd.game.content.components;

public class RotationComp extends CloneableComponent{
    public float rotation;

    public RotationComp(){}

    public RotationComp(float rotation){
        this.rotation = rotation;
    }

    @Override
    protected void reset(){
        rotation = 0;
    }

    @Override
    public RotationComp copy(){
        return new RotationComp(rotation);
    }
}
