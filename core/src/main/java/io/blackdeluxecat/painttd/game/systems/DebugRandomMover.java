package io.blackdeluxecat.painttd.game.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.game.content.components.logic.*;

public class DebugRandomMover extends IteratingSystem{
    public ComponentMapper<PositionComp> pm;

    public DebugRandomMover(){
        super(Aspect.all(PositionComp.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        pm = world.getMapper(PositionComp.class);
    }

    @Override
    protected void process(int entityId){
        PositionComp p = pm.get(entityId);
        p.x = MathUtils.clamp(p.x + MathUtils.random(-4f, 4f), 0, Gdx.graphics.getWidth());
        p.y = MathUtils.clamp(p.y + MathUtils.random(-4f, 4f), 0, Gdx.graphics.getHeight());
    }
}
