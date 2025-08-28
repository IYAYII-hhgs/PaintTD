package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.request.*;

import static io.blackdeluxecat.painttd.game.Game.damageQueue;

public class DamageTypeDirectApply extends BaseSystem{
    public DamageQueue.DamageType type;

    public ComponentMapper<DamageComp> dm;
    public ComponentMapper<HealthComp> hm;

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        type = DamageQueue.DamageType.direct;
        dm = world.getMapper(DamageComp.class);
        hm = world.getMapper(HealthComp.class);
    }

    @Override
    protected void processSystem(){
        damageQueue.clearHandled();
        for(int i = 0; i < damageQueue.queue.size; i++){
            DamageQueue.DamageRequest req = damageQueue.queue.get(i);
            if(req.type == DamageQueue.DamageType.direct){
                int src = req.sourceId;
                int tgt = req.targetId;
                HealthComp tgtHealth = hm.get(tgt);
                if(tgtHealth.health <= 0) continue;
                DamageComp srcDamage = dm.get(src);
                tgtHealth.health -= srcDamage.damage;
                req.handle();
            }
        }
    }
}
