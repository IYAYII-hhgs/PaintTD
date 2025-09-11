package io.blackdeluxecat.painttd.content.components.logic.physics;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.*;

public class HitboxComp extends CopyableComponent implements Json.Serializable{
    public float width = 1;
    public float height = 1;
    public float scale = 1;

    public HitboxComp(){
    }

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
        width = hitboxComp.width;
        height = hitboxComp.height;
        scale = hitboxComp.scale;
        return this;
    }

    @Override
    protected void reset(){
        width = 1;
        height = 1;
        scale = 1;
    }

    @Override
    public void write(Json json){
        json.writeValue("scale", scale);
    }

    @Override
    public void read(Json json, JsonValue jsonData){
        scale = jsonData.getFloat("scale");
    }

    @Override
    public void refill(CopyableComponent def){
        HitboxComp hitboxComp = (HitboxComp)def;
        width = hitboxComp.width;
        height = hitboxComp.height;
    }
}
