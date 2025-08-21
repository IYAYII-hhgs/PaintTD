package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

import static io.blackdeluxecat.painttd.Core.shaper;

public class DrawTarget extends IteratingSystem{
    public ComponentMapper<TargetComp> tm;
    public ComponentMapper<PositionComp> pm;
    public ComponentMapper<RangeComp> rm;

    public DrawTarget(){
        super(Aspect.all(TargetComp.class, PositionComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        tm = world.getMapper(TargetComp.class);
        pm = world.getMapper(PositionComp.class);
        rm = world.getMapper(RangeComp.class);
    }

    @Override
    protected void process(int entityId){
        TargetComp target = tm.get(entityId);
        PositionComp pos = pm.get(entityId), targetPos;
        if(target.targetId != -1 && pm.has(target.targetId)){
            targetPos = pm.get(target.targetId);
            shaper.begin(ShapeRenderer.ShapeType.Line);
            shaper.setColor(Color.RED);
            shaper.line(Vars.v1.set(pos.x, pos.y), Vars.v2.set(targetPos.x, targetPos.y));
            shaper.end();
        }

        RangeComp range = rm.get(entityId);
        if(range != null){
            shaper.begin(ShapeRenderer.ShapeType.Line);
            shaper.setColor(Color.DARK_GRAY);
            shaper.circle(pos.x, pos.y, range.range, 24);
            shaper.end();
        }
    }
}
