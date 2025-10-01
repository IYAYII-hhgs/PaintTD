package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

/**
 * 处理{@link DamageQueue.SlashDamageData}请求, 转换为{@link DamageQueue.DirectDamageData}
 */
@IsLogicProcess
public class DamageApplySlashDamage extends BaseSystem{
    @Override
    protected void processSystem(){
        damageQueue.clearHandled();
        for(int i = 0; i < damageQueue.queue.size; i++){
            DamageQueue.DamageRequest req = damageQueue.queue.get(i);
            if(req.data instanceof DamageQueue.SlashDamageData data){
                entities.eachCircle(data.x, data.y, data.range,
                    e -> !utils.isTeammate(req.sourceId, e),
                    e -> damageQueue.add(req.sourceId, e, DamageQueue.newData(DamageQueue.DirectDamageData.class).dmg(data.damage)));
            }
        }
    }
}
