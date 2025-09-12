package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class TileStainRequestDamage extends IteratingSystem{
    public ComponentMapper<CollideComp> collideMapper;
    public ComponentMapper<HealthComp> healthMapper;
    public ComponentMapper<TeamComp> teamMapper;
    public ComponentMapper<PositionComp> positionMapper;


    public TileStainRequestDamage(){
        super(Aspect.all(CollideComp.class, HealthComp.class, TeamComp.class, PositionComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void process(int entityId){
        CollideComp collide = collideMapper.get(entityId);
        if(collide.isCollideWith(CollideComp.OVERLAY)){
            PositionComp positionComp = positionMapper.get(entityId);
            int x = positionComp.tileX(), y = positionComp.tileY();
            if(Game.map.validPos(x, y)){
                damageQueue.add(entityId, Game.map.getTileStain(x, y), 9999, DamageQueue.DamageRequestType.collide);
            }
        }
    }
}
