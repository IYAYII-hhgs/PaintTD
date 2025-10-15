package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.systems.*;
import io.blackdeluxecat.painttd.utils.*;

@IsLogicProcess
public class ShootAtkSingleFireSplashTileStain extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<StainSplashComp> stainSplashMapper;
    ComponentMapper<TeamComp> teamMapper;

    public ShootAtkSingleFireSplashTileStain(){
        super(Aspect.all(CooldownComp.class, TargetSingleComp.class, StainSplashComp.class, MarkerComp.ShootAttacker.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                PositionComp tgtPos = positionMapper.get(targetSingle.targetId);
                StainSplashComp splash = stainSplashMapper.get(entityId);

                for(int i = 0; i < cooldown.shootCount; i++){
                    Events.fire(EventTypes.StainSplashDamageEvent.class, e -> {
                        e.source = entityId;
                        e.team = teamMapper.get(entityId).team;
                        e.x = tgtPos.tileX();
                        e.y = tgtPos.tileY();
                        e.damage = splash.damage;
                        e.radius = splash.radius;
                    });
                }
            }
        }

    }
}
