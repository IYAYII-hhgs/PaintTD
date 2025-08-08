package io.blackdeluxecat.painttd.content.components.logic.movement;

import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.content.components.logic.*;

public class VelocityComp extends CopyableComponent{
    public float x, y;
    public VelocityComp(){}

    public VelocityComp(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2 get(Vector2 out){
        return out.set(x, y);
    }

    public void set(Vector2 in){
        x = in.x;
        y = in.y;
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
