package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class CollideAtkRequestSlashDamage extends BaseSystem{
    @All(value = {DamageSlashComp.class, MarkerComp.CollideAttacker.class})
    Aspect sourceAspect;

    public ComponentMapper<DamageSlashComp> damageSlashMapper;
    public ComponentMapper<PositionComp> positionMapper;

    @Override
    protected void processSystem(){
        for(int i = 0; i < collideQueue.queue.size; i++){
            CollideQueue.CollideRequest req = collideQueue.queue.get(i);
            if(req.handled) continue;

            var e1 = world.getEntity(req.e1);
            var e2 = world.getEntity(req.e2);

            int source = sourceAspect.isInterested(e1) ? req.e1 : sourceAspect.isInterested(e2) ? req.e2 : -1;
            if(source == -1) continue;
            int target = source == req.e1 ? req.e2 : req.e1;

            if(!utils.isTeammate(source, target)){
                DamageSlashComp dmg = damageSlashMapper.get(source);
                PositionComp tgtPos = positionMapper.get(target);
                damageQueue.add(source, target, DamageQueue.newData(DamageQueue.SlashDamageData.class).pos(tgtPos.x, tgtPos.y).dmg(dmg.damage, dmg.range));
            }
        }
    }
}
