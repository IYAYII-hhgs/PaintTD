package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class CollideTileSlash extends BaseSystem{
    @All(value = {MarkerComp.Bullet.class, StainSlashComp.class})
    Aspect slashAspect;

    public ComponentMapper<TileStainComp> stainMapper;
    public ComponentMapper<StainSlashComp> stainSlashMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<TeamComp> teamMapper;

    IntArray tmp = new IntArray();

    @Override
    protected void processSystem(){
        for(int i = 0; i < collideQueue.queue.size; i++){
            CollideQueue.CollideRequest req = collideQueue.queue.get(i);
            if(req.handled) continue;

            var source = world.getEntity(req.e1);
            var target = world.getEntity(req.e2);

            if(utils.isTeammate(req.e1, req.e2)) continue;

            // 检查碰撞双方是否是染色子弹和染色地块
            int slasher = slashAspect.isInterested(source) ? req.e1 : slashAspect.isInterested(target) ? req.e2 : -1;
            if(slasher == -1) continue;
            int tgt = slasher == req.e1 ? req.e2 : req.e1;

            StainSlashComp slash = stainSlashMapper.get(slasher);
            PositionComp tgtPos = positionMapper.get(tgt);

            int x = tgtPos.tileX(), y = tgtPos.tileY();

            tmp.clear();
            map.queryCircle(map.stains, x, y, slash.range, tmp);
            for(int j = 0; j < tmp.size; j++){
                int tileStain = tmp.get(j);
                utils.putTileStain(tileStain, teamMapper.get(slasher).team, slash.level);
            }
        }
    }
}
