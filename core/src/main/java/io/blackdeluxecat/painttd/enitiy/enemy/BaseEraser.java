package io.blackdeluxecat.painttd.enitiy.enemy;

import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.enitiy.*;

public class BaseEraser extends Entity{
    public float speed = 1f;

    public BaseEraser(float x, float y){
        this.x = x;
        this.y = y;
        this.health = 5f;
    }

    public void act(){

    }

    public void move(Vector2 vec){
        vec.setLength(speed);
        x += vec.x;
        y += vec.y;
    }

    public void draw(){
        Core.batch.draw(Core.atlas.findRegion("eraser"), x, y, 32, 32);
    }
}
