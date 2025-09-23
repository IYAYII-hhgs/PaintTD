package io.blackdeluxecat.painttd.systems.utils;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

@Wire
public class HoverListener extends BaseSystem{
    @All(value = {MarkerComp.Hoverable.class, PositionComp.class, HitboxComp.class})
    EntitySubscription sub;

    ComponentMapper<PositionComp> posMapper;
    ComponentMapper<HitboxComp> hitboxMapper;

    public int hovered = -1, lastHovered;

    static Rectangle rect = new Rectangle();

    @Override
    protected void processSystem(){
        hovered = -1;
        IntBag ids = sub.getEntities();
        for(int i = 0; i < ids.size(); i++){
            int e = ids.get(i);
            PositionComp pos = posMapper.get(e);
            HitboxComp hitbox = hitboxMapper.get(e);
            if(rect.setSize(hitbox.width, hitbox.height).setCenter(pos.x, pos.y)
                   .contains(Vars.worldViewport.unproject(Vars.v1.set(Gdx.input.getX(), Gdx.input.getY()))
                   )){
                hovered = e;
            }
        }
        if(lastHovered != hovered){
            lastHovered = hovered;
            Vars.hud.hoveredTable.build(hovered);
        }

    }
}
