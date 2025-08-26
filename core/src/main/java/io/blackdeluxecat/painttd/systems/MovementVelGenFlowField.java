package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.*;

public class MovementVelGenFlowField extends IteratingSystem{
    private ComponentMapper<PositionComp> positionMapper;
    private ComponentMapper<VelocityComp> velocityMapper;
    private ComponentMapper<MoveSpeedComp> moveSpeedMapper;

    public MovementVelGenFlowField() {
        super(Aspect.all(PositionComp.class, VelocityComp.class, MoveSpeedComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        positionMapper = world.getMapper(PositionComp.class);
        velocityMapper = world.getMapper(VelocityComp.class);
        moveSpeedMapper = world.getMapper(MoveSpeedComp.class);
    }

    @Override
    protected void process(int entityId) {
        PositionComp pos = positionMapper.get(entityId);
        VelocityComp vel = velocityMapper.get(entityId);
        MoveSpeedComp moveSpeed = moveSpeedMapper.get(entityId);

        // 获取当前位置的方向
        vel.set(Game.flowField.getDirection((int)pos.x, (int)pos.y, Vars.v1).scl(moveSpeed.speed));
    }
}
