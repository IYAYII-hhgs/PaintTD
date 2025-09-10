package io.blackdeluxecat.painttd.systems.utils;

import com.artemis.*;
import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;

@Wire
public class StaticUtils extends BaseSystem{
    @All public EntitySubscription allEntitiesSub;

    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<HitboxComp> hitboxMapper;
    public ComponentMapper<HealthComp> healthMapper;
    public ComponentMapper<TeamComp> teamMapper;
    public ComponentMapper<EnergyComp> energyMapper;
    public ComponentMapper<EnergyRegenComp> energyRegenMapper;
    public ComponentMapper<TileStainComp> tileStainMapper;

    public boolean isTeammate(int e1, int e2){
        return teamMapper.get(e1).team == teamMapper.get(e2).team;
    }

    public void setPosition(int e, float x, float y){
        PositionComp p = positionMapper.get(e);
        p.x = x;
        p.y = y;
    }

    public void setEnergySystem(int e, float cap, float init, boolean hasRegen, float regenRate){
        EnergyComp ec = energyMapper.get(e);
        if(ec != null){
            ec.energy = init;
            ec.maxEnergy = cap;
        }

        EnergyRegenComp erc = energyRegenMapper.get(e);
        if(hasRegen && erc != null){
            erc.regenRate = regenRate;
        }
    }

    @Override
    protected void processSystem(){
    }
}
