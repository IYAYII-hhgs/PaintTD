package io.blackdeluxecat.painttd.systems.utils;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

public class StaticUtils extends BaseSystem{
    @All public EntitySubscription allEntitiesSub;

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
        return teamMapper.get(e1).team == teamMapper.get(e2).team;
    }

    public void setPosition(int e, float x, float y){
        PositionComp p = positionMapper.get(e);
        p.x = x;
        p.y = y;
    }

    @Override
    protected void processSystem(){
    }
}
