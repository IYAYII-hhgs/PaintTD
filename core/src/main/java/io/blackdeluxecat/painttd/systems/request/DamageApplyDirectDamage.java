package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

/**
 * 处理{@link DamageQueue.DirectDamageData}请求
 */

@IsLogicProcess
public class DamageApplyDirectDamage extends BaseSystem{
    ComponentMapper<HealthComp> hm;

    @Override
    protected void processSystem(){
        damageQueue.clearHandled();
        for(int i = 0; i < damageQueue.queue.size; i++){
            DamageQueue.DamageRequest req = damageQueue.queue.get(i);
            if(req.data instanceof DamageQueue.DirectDamageData data){
                int tgt = req.targetId;
                HealthComp tgtHealth = hm.get(tgt);
                if(tgtHealth.health <= 0) continue;
                tgtHealth.health -= data.damage;
                req.handle();
            }
        }
    }
}
