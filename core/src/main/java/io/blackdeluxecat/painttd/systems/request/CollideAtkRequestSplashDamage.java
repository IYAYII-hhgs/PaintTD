package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class CollideAtkRequestSplashDamage extends BaseSystem{
    @All(value = {DamageSplashComp.class, MarkerComp.CollideAttacker.class})
    Aspect sourceAspect;

    public ComponentMapper<DamageSplashComp> damageSplashMapper;
    public ComponentMapper<PositionComp> positionMapper;

    @Override
    protected void processSystem(){
        for(int i = 0; i < collideQueue.queue.size; i++){
            CollideQueue.CollideRequest req = collideQueue.queue.get(i);
            if(req.handled) continue;

            var e1 = world.getEntity(req.source);
            var e2 = world.getEntity(req.target);

            int source = sourceAspect.isInterested(e1) ? req.source : sourceAspect.isInterested(e2) ? req.target : -1;
            if(source == -1) continue;
            int target = source == req.source ? req.target : req.source;

            if(!utils.isTeammate(source, target)){
                DamageSplashComp dmg = damageSplashMapper.get(source);
                PositionComp tgtPos = positionMapper.get(target);
                damageQueue.add(source, target, DamageQueue.newData(DamageQueue.SplashDamageData.class).pos(tgtPos.x, tgtPos.y).dmg(dmg.damage, dmg.range));
            }
        }
    }
}
