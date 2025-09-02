package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.request.*;

import static io.blackdeluxecat.painttd.game.Game.damageQueue;

public class DamageTypeCollideApply extends BaseSystem{
    public DamageQueue.DamageRequestType type;

    public ComponentMapper<HealthComp> healthMapper;

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        type = DamageQueue.DamageRequestType.collide;
        healthMapper = world.getMapper(HealthComp.class);
    }

    @Override
    protected void processSystem(){
        damageQueue.clearHandled();
        for(int i = 0; i < damageQueue.queue.size; i++){
            DamageQueue.DamageRequest req = damageQueue.queue.get(i);
            if(req.type == type){
                if(healthMapper.has(req.sourceId) && healthMapper.has(req.targetId)){
                    var sourceHealth = healthMapper.get(req.targetId);
                    var targetHealth = healthMapper.get(req.sourceId);
                    if(sourceHealth.health <= 0 || targetHealth.health <=0){
                        continue;
                    }
                    float damage = Math.min(sourceHealth.health, targetHealth.health);
                    targetHealth.health -= damage;
                    sourceHealth.health -= damage;
                    req.handle();
                }
            }
        }
    }
}
