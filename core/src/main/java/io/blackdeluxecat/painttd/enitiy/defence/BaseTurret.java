package io.blackdeluxecat.painttd.enitiy.defence;

import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.enitiy.*;

public class BaseTurret extends Entity{
    public float range = 100;
    public void act(){

    }

    public void move(Vector2 vec){}

    public void draw(){
        Core.batch.draw(Core.atlas.findRegion("turret"), x, y, 32, 32);
    }
}
