package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.request.*;

import static io.blackdeluxecat.painttd.game.Game.*;

public class CollideEnemyRequestDamage extends BaseSystem{
    Aspect.Builder builder;
    Aspect aspect;

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        builder = Aspect.all(TeamComp.class, HealthComp.class).exclude(MarkerComp.Dead.class);
        aspect = builder.build(world);
    }

    @Override
    protected void processSystem(){
        collideQueue.clearHandled();

        for(CollideQueue.CollideRequest collideRequest : collideQueue.queue){
            if(collideRequest.handled) continue;

            var source = world.getEntity(collideRequest.e1);
            var target = world.getEntity(collideRequest.e2);

            if(aspect.isInterested(source) && aspect.isInterested(target)){
                if(!utils.isTeammate(source.getId(), target.getId())){
                    damageQueue.add(collideRequest.e1, collideRequest.e2, DamageQueue.DamageType.collide);
                }
            }
        }
    }
}