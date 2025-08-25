package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

import java.lang.reflect.*;

import static io.blackdeluxecat.painttd.game.Game.map;
import static io.blackdeluxecat.painttd.game.Game.pathfinder;

public class MovementVelGenPathfind extends IteratingSystem{
    public ComponentMapper<PathfindComp> pathfindMapper;
    public ComponentMapper<MoveSpeedComp> moveSpeedMapper;
    public ComponentMapper<VelocityComp> velocityMapper;
    public ComponentMapper<PositionComp> positionMapper;

    public static Vector2 tmp = new Vector2();

    public MovementVelGenPathfind(){
        super(Aspect.all(PathfindComp.class, MoveSpeedComp.class, VelocityComp.class, PositionComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        pathfindMapper = world.getMapper(PathfindComp.class);
        moveSpeedMapper = world.getMapper(MoveSpeedComp.class);
        velocityMapper = world.getMapper(VelocityComp.class);
        positionMapper = world.getMapper(PositionComp.class);
    }

    @Override
    protected void process(int entityId){
        PathfindComp pathComp = pathfindMapper.get(entityId);
        if (!pathComp.isMoving || pathComp.currentPath == null) return;

        PositionComp pos = positionMapper.get(entityId);
        VelocityComp vel = velocityMapper.get(entityId);
        MoveSpeedComp moveSpeed = moveSpeedMapper.get(entityId);

        // 如果没有路径或到达终点，重新计算路径
        if (pathComp.currentPath == null || pathComp.currentPath.isEmpty() || pathComp.currentTargetIndex >= pathComp.currentPath.size) {
            Array<Vector2> path = pathfinder.findPath(pos.x, pos.y, (float)map.width / 2, (float)map.height / 2);
            if(path != null){
                pathComp.currentPath = path;
                pathComp.currentTargetIndex = 0;
            }
        }

        if (pathComp.currentTargetIndex < pathComp.currentPath.size) {
            Vector2 target = pathComp.currentPath.get(pathComp.currentTargetIndex);
            tmp.set(target);
            vel.set(tmp.set(target).sub(pos.x, pos.y).nor().scl(moveSpeed.speed));

            // 检查是否到达当前目标点
            if (target.dst(pos.x, pos.y) < 0.1f) {
                pathComp.currentTargetIndex++;
            }
        }
    }
}
