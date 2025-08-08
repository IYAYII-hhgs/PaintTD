package io.blackdeluxecat.painttd.content.components.logic.movement;

import io.blackdeluxecat.painttd.content.components.logic.*;

public class MoveSpeedComp extends CopyableComponent{
    public float baseSpeed, speed;
    public MoveSpeedComp(){}

    public MoveSpeedComp(float baseSpeed){
        this.baseSpeed = baseSpeed;
        this.speed = baseSpeed;
    }

    @Override
    public MoveSpeedComp copy(CopyableComponent other){
        MoveSpeedComp moveSpeedComp = (MoveSpeedComp)other;
        moveSpeedComp.baseSpeed = baseSpeed;
        moveSpeedComp.speed = speed;
        return moveSpeedComp;
    }

    @Override
    protected void reset(){
        speed = baseSpeed;
    }
}
