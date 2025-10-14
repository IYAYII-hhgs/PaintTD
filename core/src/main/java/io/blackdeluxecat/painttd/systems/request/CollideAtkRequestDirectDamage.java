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

            if(req.source == -1 || !sourceAspect.isInterested(world.getEntity(req.source))) continue;

            if(!utils.isTeammate(req.source, req.target)){
                DamageComp dmg = damageMapper.get(req.source);
                damageQueue.add(req.source, req.target, DamageQueue.newData(DamageQueue.DirectDamageData.class).dmg(dmg.damage));
            }
        }
    }
}
