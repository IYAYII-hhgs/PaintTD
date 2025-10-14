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
public class CollideTileSplash extends BaseSystem{
    @All(value = {MarkerComp.CollideAttacker.class, StainSplashComp.class})
    Aspect splashAspect;

    public ComponentMapper<StainSplashComp> stainSplashMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<TeamComp> teamMapper;

    IntArray tmp = new IntArray();

    @Override
    protected void processSystem(){
        for(int i = 0; i < collideQueue.queue.size; i++){
            CollideQueue.CollideRequest req = collideQueue.queue.get(i);
            if(req.handled) continue;

            var source = world.getEntity(req.source);
            var target = world.getEntity(req.target);

            if(utils.isTeammate(req.source, req.target)) continue;

            // 检查碰撞双方是否是染色子弹和染色地块
            int splasher = splashAspect.isInterested(source) ? req.source : splashAspect.isInterested(target) ? req.target : -1;
            if(splasher == -1) continue;
            int tgt = splasher == req.source ? req.target : req.source;

            StainSplashComp splash = stainSplashMapper.get(splasher);
            PositionComp tgtPos = positionMapper.get(tgt);

            int x = tgtPos.tileX(), y = tgtPos.tileY();

            tmp.clear();
            map.queryCircle(map.stains, x, y, splash.range, tmp);
            for(int j = 0; j < tmp.size; j++){
                int tileStain = tmp.get(j);
                utils.putTileStain(tileStain, teamMapper.get(splasher).team, splash.damage);
            }
        }
    }
}
