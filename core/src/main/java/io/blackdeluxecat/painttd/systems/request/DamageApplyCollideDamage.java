package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

/**
 * 处理{@link DamageQueue.CollideDamageData}请求
 */

@IsLogicProcess
public class DamageApplyCollideDamage extends BaseSystem{
    public ComponentMapper<HealthComp> healthMapper;

    @Override
    protected void processSystem(){
        damageQueue.clearHandled();
        for(int i = 0; i < damageQueue.queue.size; i++){
            DamageQueue.DamageRequest req = damageQueue.queue.get(i);
            if(req.data instanceof DamageQueue.CollideDamageData){
                if(healthMapper.has(req.sourceId) && healthMapper.has(req.targetId)){
                    var sourceHealth = healthMapper.get(req.targetId);
                    var targetHealth = healthMapper.get(req.sourceId);
                    if(sourceHealth.health <= 0 || targetHealth.health <= 0){
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
