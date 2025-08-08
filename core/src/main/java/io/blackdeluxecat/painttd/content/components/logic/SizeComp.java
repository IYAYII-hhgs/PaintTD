package io.blackdeluxecat.painttd.content.components.logic;

public class SizeComp extends CopyableComponent{
    public float width = 1;
    public float height = 1;
    public float scale = 1;

    public SizeComp(){}

    public SizeComp(float width, float height){
        this.width = width;
        this.height = height;
    }

    public SizeComp(float size){
        this.width = size;
        this.height = size;
    }

    @Override
    public SizeComp copy(CopyableComponent other){
        SizeComp sizeComp = (SizeComp)other;
        sizeComp.width = width;
        sizeComp.height = height;
        sizeComp.scale = scale;
        return sizeComp;
    }

    @Override
    protected void reset(){
        width = 1;
        height = 1;
        scale = 1;
    }
}
