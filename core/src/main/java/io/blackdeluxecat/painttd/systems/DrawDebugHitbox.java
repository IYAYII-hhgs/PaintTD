package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;

import static io.blackdeluxecat.painttd.Core.*;

public class DrawDebugHitbox extends IteratingSystem{
    public ComponentMapper<PositionComp> pm;
    public ComponentMapper<HealthComp> hm;

    public DrawDebugHitbox(){
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
        Vars.c1.set(Color.WHITE);
        HealthComp hpc = hm.get(entityId);
        if(hpc != null){
            Vars.c1.lerp(Color.RED, 1 - hpc.health / hpc.maxHealth);
        }
        shaper.setColor(Vars.c1);
        shaper.rect(pos.x - 0.5f, pos.y - 0.5f, 1, 1);
        shaper.end();
    }
}
