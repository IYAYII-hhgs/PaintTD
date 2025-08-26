package io.blackdeluxecat.painttd.systems;

import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.content.components.*;

public class MovementNextPathComp extends CopyableComponent{
    public Vector2 current = new Vector2(), next = new Vector2();

    public MovementNextPathComp(){}

    @Override
    protected void reset(){
        current.setZero();
        next.setZero();
    }

    @Override
    public MovementNextPathComp copy(CopyableComponent other){
        return this;
    }
}
