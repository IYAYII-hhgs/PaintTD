package io.blackdeluxecat.painttd.systems.render;

import com.artemis.*;
import com.artemis.utils.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;

import static io.blackdeluxecat.painttd.Core.shaper;
import static io.blackdeluxecat.painttd.game.Game.groups;
import static io.blackdeluxecat.painttd.game.Game.map;

public class DrawColoredTileStain extends BaseSystem{
    public ComponentMapper<HealthComp> healthMapper;
    public ComponentMapper<PositionComp> positionMapper;

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        healthMapper = world.getMapper(HealthComp.class);
        positionMapper = world.getMapper(PositionComp.class);
    }

    @Override
    protected void processSystem(){
        shaper.begin(ShapeRenderer.ShapeType.Filled);
        IntBag bag = (IntBag)groups.getEntityIds("tileStain");
        for(int i = 0; i < bag.size(); i++){
            int e = bag.get(i);
            var health = healthMapper.get(e);
            var position = positionMapper.get(e);
            var color = map.colorPalette.getColor(MathUtils.ceil(health.health));
            shaper.setColor(Vars.c1.set(color));
            shaper.rect(position.x - 0.5f, position.y - 0.5f, 1, 1);
        }
        shaper.end();
    }
}
