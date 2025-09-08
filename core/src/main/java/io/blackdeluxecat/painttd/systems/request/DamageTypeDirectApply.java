package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.damageQueue;

@IsLogicProcess
public class DamageTypeDirectApply extends BaseSystem{
    public DamageQueue.DamageRequestType type;

    public ComponentMapper<HealthComp> hm;

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        type = DamageQueue.DamageRequestType.direct;
        hm = world.getMapper(HealthComp.class);
    }

    @Override
    protected void processSystem(){
        damageQueue.clearHandled();
        for(int i = 0; i < damageQueue.queue.size; i++){
            DamageQueue.DamageRequest req = damageQueue.queue.get(i);
            if(req.type == DamageQueue.DamageRequestType.direct){
                int tgt = req.targetId;
                HealthComp tgtHealth = hm.get(tgt);
                if(tgtHealth.health <= 0) continue;
                tgtHealth.health -= req.amount;
                req.handle();
            }
        }
    }
}
