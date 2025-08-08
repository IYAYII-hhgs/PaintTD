package io.blackdeluxecat.painttd.content.components.logic;

public class RotationComp extends CopyableComponent{
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
    public RotationComp copy(CopyableComponent other){
        RotationComp rotationComp = (RotationComp) other;
        rotationComp.rotation = rotation;
        return rotationComp;
    }
}
