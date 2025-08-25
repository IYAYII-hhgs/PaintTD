package io.blackdeluxecat.painttd.content.components.logic.physics;

import io.blackdeluxecat.painttd.content.components.*;

public class MoveSpeedComp extends CopyableComponent{
    public float baseSpeed, speed;
    public MoveSpeedComp(){}

    public MoveSpeedComp(float baseSpeedUnitPerTick){
        this.baseSpeed = baseSpeedUnitPerTick;
        this.speed = baseSpeedUnitPerTick;
    }

    @Override
    public MoveSpeedComp copy(CopyableComponent other){
        MoveSpeedComp moveSpeedComp = (MoveSpeedComp)other;
        baseSpeed = moveSpeedComp.baseSpeed;
        speed = moveSpeedComp.speed;
        return moveSpeedComp;
    }

    @Override
    protected void reset(){
        speed = baseSpeed;
    }
}
