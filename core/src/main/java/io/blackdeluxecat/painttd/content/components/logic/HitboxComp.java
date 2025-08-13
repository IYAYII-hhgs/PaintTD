package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class HitboxComp extends CopyableComponent{
    public float width = 1;
    public float height = 1;
    public float scale = 1;

    public HitboxComp(){}

    public HitboxComp(float width, float height){
        this.width = width;
        this.height = height;
    }

    public HitboxComp(float size){
        this.width = size;
        this.height = size;
    }

    @Override
    public HitboxComp copy(CopyableComponent other){
        HitboxComp hitboxComp = (HitboxComp)other;
        hitboxComp.width = width;
        hitboxComp.height = height;
        hitboxComp.scale = scale;
        return hitboxComp;
    }

    @Override
    protected void reset(){
        width = 1;
        height = 1;
        scale = 1;
    }
}
