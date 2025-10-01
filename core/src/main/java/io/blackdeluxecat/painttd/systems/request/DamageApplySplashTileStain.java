package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

/**
 * 处理{@link DamageQueue.SplashTileStainData}请求, 转换为{@link DamageQueue.DirectTileStainData}
 */
@IsLogicProcess
public class DamageApplySplashTileStain extends BaseSystem{
    ComponentMapper<PositionComp> pm;
    IntArray tmp = new IntArray();

    @Override
    protected void processSystem(){
        damageQueue.clearHandled();
        for(int i = 0; i < damageQueue.queue.size; i++){
            DamageQueue.DamageRequest req = damageQueue.queue.get(i);
            if(req.data instanceof DamageQueue.SplashTileStainData data){
                tmp.clear();
                map.queryCircle(map.stains, data.x, data.y, (int)data.range, tmp);
                for(int j = 0; j < tmp.size; j++){
                    int tileStain = tmp.get(j);
                    PositionComp pos = pm.get(tileStain);
                    damageQueue.add(req.sourceId, tileStain, DamageQueue.newData(DamageQueue.DirectTileStainData.class).dmg(data.damage).pos(pos.tileX(), pos.tileY()));
                }
            }
        }
    }
}
