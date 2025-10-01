package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class CollideRequestDamage extends BaseSystem{
    @All(value = {TeamComp.class, HealthComp.class})
    @Exclude(MarkerComp.Dead.class)
    Aspect aspect;

    @Override
    protected void processSystem(){
        for(int i = 0; i < collideQueue.queue.size; i++){
            CollideQueue.CollideRequest req = collideQueue.queue.get(i);
            if(req.handled) continue;
            var source = world.getEntity(req.e1);
            var target = world.getEntity(req.e2);

            if(aspect.isInterested(source) && aspect.isInterested(target)){
                if(!utils.isTeammate(source.getId(), target.getId())){
                    damageQueue.add(req.e1, req.e2, DamageQueue.newData(DamageQueue.CollideDamageData.class));
                    //req.handle();
                }
            }
        }
    }
}