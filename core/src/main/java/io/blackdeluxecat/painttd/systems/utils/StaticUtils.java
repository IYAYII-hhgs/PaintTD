package io.blackdeluxecat.painttd.systems.utils;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;

import static io.blackdeluxecat.painttd.game.Game.world;

public class StaticUtils extends BaseSystem{
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<HitboxComp> hitboxMapper;
    public ComponentMapper<HealthComp> healthMapper;
    public ComponentMapper<TeamComp> teamMapper;
    public ComponentMapper<EnergyComp> energyMapper;
    public ComponentMapper<EnergyRegenComp> energyRegenMapper;

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
    protected void processSystem(){}

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        positionMapper = world.getMapper(PositionComp.class);
        hitboxMapper = world.getMapper(HitboxComp.class);
        healthMapper = world.getMapper(HealthComp.class);
        teamMapper = world.getMapper(TeamComp.class);
        energyMapper = world.getMapper(EnergyComp.class);
        energyRegenMapper = world.getMapper(EnergyRegenComp.class);
    }
}
