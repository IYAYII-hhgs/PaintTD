package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.struct.func.*;

/**
 * 一个基础索敌系统. 根据索敌标识使用过滤器筛选 **一个** 敌人.
 * */
public class TargetFind extends IteratingSystem{
    public ComponentMapper<PositionComp> pm;
    public ComponentMapper<TargetComp> tcm;
    public ComponentMapper<TargetPriorityComp> tpm;
    public ComponentMapper<MarkerComp.Dead> dm;
    public ComponentMapper<RangeComp> rm;

    protected final Vector2 entityPos = new Vector2();
    protected int currentProcessId;

    public TargetFind(){
        super(Aspect.all(PositionComp.class, TargetComp.class, TargetPriorityComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        pm = world.getMapper(PositionComp.class);
        tcm = world.getMapper(TargetComp.class);
        tpm = world.getMapper(TargetPriorityComp.class);
        dm = world.getMapper(MarkerComp.Dead.class);
        rm = world.getMapper(RangeComp.class);
    }

    @Override
    protected void process(int entityId){
        currentProcessId = entityId;
        TargetComp current = tcm.get(entityId);
        TargetPriorityComp priority =  tpm.get(entityId);
        PositionComp pos = pm.get(entityId);
        RangeComp range = rm.get(entityId);
        if(current.targetId == -1 || dm.get(current.targetId) != null){
            entityPos.set(pos.x, pos.y);
            current.targetId = Game.entities.closestCircle(pos.x, pos.y, range.range, getDistanceFilter(priority.priority));
        }
    }

    public IntFloatf getDistanceFilter(int priority){
        return switch(priority){
            case TargetPriorityComp.CLOSEST -> (id) -> {
                if(currentProcessId == id) return Float.MAX_VALUE;  //排除自身
                PositionComp pos = pm.get(id);
                return Vars.v1.set(pos.x, pos.y).sub(entityPos).len2();
            };
            default -> (e) -> {
                return -1;
            };
        };
    }
}
