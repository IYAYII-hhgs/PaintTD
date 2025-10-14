package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class CollideAtkRequestSplashTileStain extends BaseSystem{
    @All(value = {StainSplashComp.class, MarkerComp.CollideAttacker.class})
    Aspect sourceAspect;

    public ComponentMapper<StainSplashComp> stainSplashMapper;
    public ComponentMapper<PositionComp> positionMapper;

    @Override
    protected void processSystem(){
        for(int i = 0; i < collideQueue.queue.size; i++){
            CollideQueue.CollideRequest req = collideQueue.queue.get(i);
            if(req.handled) continue;

            if(req.source == -1 || !sourceAspect.isInterested(world.getEntity(req.source))) continue;

            if(!utils.isTeammate(req.source, req.target)){
                StainSplashComp dmg = stainSplashMapper.get(req.source);
                PositionComp tgtPos = positionMapper.get(req.target);
                damageQueue.add(req.source, req.target, DamageQueue.newData(DamageQueue.SplashDamageData.class).pos(tgtPos.x, tgtPos.y).dmg(dmg.damage, dmg.range));
            }
        }
    }
}
