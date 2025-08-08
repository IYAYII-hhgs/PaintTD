package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;

import static io.blackdeluxecat.painttd.Core.*;

public class DebugDrawer extends IteratingSystem{
    public ComponentMapper<PositionComp> pm;

    public DebugDrawer(){
        super(Aspect.all(PositionComp.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        pm = world.getMapper(PositionComp.class);
    }

    @Override
    protected void process(int entityId){
        PositionComp pos = pm.get(entityId);
        shaper.begin(ShapeRenderer.ShapeType.Line);
        shaper.setColor(Color.WHITE);
        shaper.rect(pos.x, pos.y, 16, 16);
        shaper.end();
    }
}
