package io.blackdeluxecat.painttd.content.components.logic.physics;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.*;

public class HitboxComp extends CopyableComponent implements Json.Serializable{
    public float x = 1, y = 1;
    public float z = 1;
    public float scale = 1;

    public HitboxComp(){
    }

    public HitboxComp(float x, float y){
        this.x = x;
        this.y = y;
    }

    public HitboxComp(float size){
        this.x = size;
        this.y = size;
    }

    public float x(){
        return x * scale;
    }

    public float y(){
        return y * scale;
    }

    //as a 2.5d game, it is usually useless
    public float z(){
        return z * scale;
    }

    @Override
    public HitboxComp copy(CopyableComponent other){
        HitboxComp hitboxComp = (HitboxComp)other;
        x = hitboxComp.x;
        y = hitboxComp.y;
        scale = hitboxComp.scale;
        return this;
    }

    @Override
    protected void reset(){
        x = 1;
        y = 1;
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
        x = hitboxComp.x;
        y = hitboxComp.y;
    }
}
