package io.blackdeluxecat.painttd.systems.utils;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

public class StaticUtils extends BaseSystem{
    @All
    public EntitySubscription allEntitiesSub;

    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<HitboxComp> hitboxMapper;
    public ComponentMapper<HealthComp> healthMapper;
    public ComponentMapper<TeamComp> teamMapper;
    public ComponentMapper<TileStainComp> tileStainMapper;
    public ComponentMapper<EntityTypeComp> entityTypeMapper;
    public ComponentMapper<TileComp> tileMapper;

    public void clearWorld(){
        IntBag es = allEntitiesSub.getEntities();
        for(int i = es.size() - 1; i >= 0; i--){
            world.delete(es.get(i));
        }
    }

    public boolean isTeammate(int e1, int e2){
        var t1 = teamMapper.get(e1);
        var t2 = teamMapper.get(e2);
        if(t1 == null || t2 == null) return false;
        return t1.team == t2.team;
    }

    public void setPosition(int e, float x, float y){
        PositionComp p = positionMapper.get(e);
        p.x = x;
        p.y = y;
    }

    /**
     * @param tgtTeam 染色源自哪一队伍, 设为-1, 将血量清空, 队伍中立
     */
    public void putTileStain(int stainId, int tgtTeam, float healthAmt){
        TileStainComp tileStain = tileStainMapper.get(stainId);
        HealthComp health = healthMapper.get(stainId);
        TeamComp team = teamMapper.get(stainId);

        if(tileStain != null && health != null && team != null){
            if(team.team != tgtTeam){
                health.health -= healthAmt;
                if(health.health <= 0){
                    team.team = tgtTeam;
                    if(tgtTeam != -1) health.health = -health.health;
                }
            }else{
                health.health = Math.max(health.health, healthAmt);
            }
        }
    }

    @Override
    protected void processSystem(){
    }
}
