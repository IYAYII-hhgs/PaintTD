package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class CollideAtkRequestDirectDamage extends BaseSystem{
    @All(value = {DamageComp.class, MarkerComp.CollideAttacker.class})
    Aspect sourceAspect;

    public ComponentMapper<DamageComp> damageMapper;

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
                DamageComp dmg = damageMapper.get(source);
                damageQueue.add(source, target, DamageQueue.newData(DamageQueue.DirectDamageData.class).dmg(dmg.damage));
            }
        }
    }
}
