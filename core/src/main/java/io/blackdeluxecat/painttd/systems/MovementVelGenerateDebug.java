package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.*;

public class MovementVelGenerateDebug extends IteratingSystem{
    public ComponentMapper<MoveSpeedComp> moveSpeedMapper;
    public ComponentMapper<VelocityComp> velocityMapper;
    public ComponentMapper<PositionComp> positionMapper;

    public static Vector2 tmp = new Vector2();

    public MovementVelGenerateDebug(){
        super(Aspect.all(MoveSpeedComp.class, VelocityComp.class, PositionComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        moveSpeedMapper = world.getMapper(MoveSpeedComp.class);
        velocityMapper = world.getMapper(VelocityComp.class);
        positionMapper = world.getMapper(PositionComp.class);
    }

    @Override
    protected void process(int entityId){
        MoveSpeedComp moveSpeedComp = moveSpeedMapper.get(entityId);
        VelocityComp velocityComp = velocityMapper.get(entityId);
        PositionComp positionComp = positionMapper.get(entityId);

        tmp.set(Game.map.width / 2f, Game.map.height / 2f).sub(positionComp.x, positionComp.y).setLength(moveSpeedComp.speed);
        velocityComp.x = tmp.x;
        velocityComp.y = tmp.y;
    }
}
