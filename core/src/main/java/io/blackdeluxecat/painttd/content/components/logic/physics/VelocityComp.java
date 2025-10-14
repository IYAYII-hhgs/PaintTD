package io.blackdeluxecat.painttd.content.components.logic.physics;

import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.content.components.*;

public class VelocityComp extends CopyableComponent{
    public float x, y;
    public float z;

    public VelocityComp(){
    }

    public Vector2 get(Vector2 out){
        return out.set(x, y);
    }

    public void set(Vector2 in){
        x = in.x;
        y = in.y;
    }

    public VelocityComp z(float z){
        this.z = z;
        return this;
    }

    @Override
    protected void reset(){
        x = 0;
        y = 0;
    }

    @Override
    public VelocityComp copy(CopyableComponent other){
        VelocityComp velocityComp = (VelocityComp)other;
        velocityComp.x = x;
        velocityComp.y = y;
        return velocityComp;
    }
}
