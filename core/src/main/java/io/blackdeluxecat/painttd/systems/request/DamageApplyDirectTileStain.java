package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

/**
 * 处理{@link DamageQueue.DirectTileStainData}请求.
 */
@IsLogicProcess
public class DamageApplyDirectTileStain extends BaseSystem{
    ComponentMapper<TeamComp> tm;

    @Override
    protected void processSystem(){
        damageQueue.clearHandled();
        for(int i = 0; i < damageQueue.queue.size; i++){
            DamageQueue.DamageRequest req = damageQueue.queue.get(i);
            if(req.data instanceof DamageQueue.DirectTileStainData data){
                var team = tm.get(req.sourceId);
                utils.putTileStain(map.getTileStain(data.x, data.y), team == null ? -1 : team.team, data.damage);
            }
        }
    }
}
