package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class CollideRequestSlashDamage extends BaseSystem{
    @All(value = {DamageSlashComp.class})
    Aspect slashAspect;

    public ComponentMapper<DamageSlashComp> damageSlashMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<TeamComp> teamMapper;

    @Override
    protected void processSystem(){
        for(int i = 0; i < collideQueue.queue.size; i++){
            CollideQueue.CollideRequest req = collideQueue.queue.get(i);
            if(req.handled) continue;

            var source = world.getEntity(req.e1);
            var target = world.getEntity(req.e2);

            if(!slashAspect.isInterested(source)) continue;
            DamageSlashComp slash = damageSlashMapper.get(req.e1);
            PositionComp tgtPos = positionMapper.get(req.e2);

            int teamSelf = teamMapper.get(req.e1).team;
            if(!utils.isTeammate(source.getId(), target.getId())){
                entities.eachCircle(tgtPos.x, tgtPos.y, slash.range, null, e -> {
                    if(teamMapper.get(e).team != teamSelf){
                        damageQueue.add(req.e1, e, slash.damage, DamageQueue.DamageRequestType.direct);
                    }
                });
            }
        }
    }
}
