package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.systems.*;
import io.blackdeluxecat.painttd.utils.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class CollideTileStainFireDamageEvents extends IteratingSystem{
    public ComponentMapper<CollideComp> collideMapper;
    public ComponentMapper<HitboxComp> hitboxMapper;
    public ComponentMapper<PositionComp> positionMapper;

    public CollideTileStainFireDamageEvents(){
        super(Aspect.all(CollideComp.class, HealthComp.class, TeamComp.class, PositionComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void process(int entityId){
        if(true) return;
        CollideComp collide = collideMapper.get(entityId);
        if(collide.canCollide(CollideComp.OVERLAY)){
            PositionComp positionComp = positionMapper.get(entityId);
            HitboxComp hitboxComp = hitboxMapper.get(entityId);

            int cx = positionComp.tileX(), cy = positionComp.tileY();

            int minx = MathUtils.floor(cx - hitboxComp.x() / 2f + 0.5f), maxx = MathUtils.ceil(cx + hitboxComp.x() / 2f - 0.5f);
            int miny = MathUtils.floor(cy - hitboxComp.y() / 2f + 0.5f), maxy = MathUtils.ceil(cy + hitboxComp.y() / 2f - 0.5f);

            for(int x = minx; x <= maxx; x++){
                for(int y = miny; y <= maxy; y++){
                    if(Game.map.validPos(x, y)){
                        int stainId = map.getTileStain(x, y);
                        Events.fire(EventTypes.CollideDamageEvent.class, e -> {
                            e.source = entityId;
                            e.target = stainId;
                        });
                    }
                }
            }

        }
    }
}
