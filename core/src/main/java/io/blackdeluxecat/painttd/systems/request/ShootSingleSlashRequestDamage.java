package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.game.request.*;

import static io.blackdeluxecat.painttd.game.Game.damageQueue;
import static io.blackdeluxecat.painttd.game.Game.entities;

public class ShootSingleSlashRequestDamage extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<DamageSlashComp> damageSlashMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<TeamComp> teamMapper;

    public ShootSingleSlashRequestDamage(){
        super(Aspect.all(CooldownComp.class, DamageSlashComp.class, TargetSingleComp.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        cooldownMapper = world.getMapper(CooldownComp.class);
        targetSingleMapper = world.getMapper(TargetSingleComp.class);
        damageSlashMapper = world.getMapper(DamageSlashComp.class);
        positionMapper = world.getMapper(PositionComp.class);
        teamMapper = world.getMapper(TeamComp.class);
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                PositionComp tgtPos = positionMapper.get(targetSingle.targetId);
                DamageSlashComp slash = damageSlashMapper.get(entityId);
                int teamSelf = teamMapper.get(entityId).team;
                for(int i = 0; i < cooldown.shootCount; i++){
                    entities.eachCircle(tgtPos.x, tgtPos.y, slash.range, null, e -> {
                        if(teamMapper.get(e).team != teamSelf){
                            damageQueue.add(entityId, e, slash.damage, DamageQueue.DamageRequestType.direct);
                        }
                    });

                }

            }
        }
    }
}
