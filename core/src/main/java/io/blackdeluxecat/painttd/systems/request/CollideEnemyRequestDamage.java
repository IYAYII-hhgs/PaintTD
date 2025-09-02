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
        for(int i = 0; i < collideQueue.queue.size; i++){
            CollideQueue.CollideRequest req = collideQueue.queue.get(i);
            if(req.handled) continue;
            var source = world.getEntity(req.e1);
            var target = world.getEntity(req.e2);

            if(aspect.isInterested(source) && aspect.isInterested(target)){
                if(!utils.isTeammate(source.getId(), target.getId())){
                    damageQueue.add(req.e1, req.e2, 9999, DamageQueue.DamageRequestType.collide);
                    req.handle();
                }
            }
        }
    }
}