package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;

import static io.blackdeluxecat.painttd.game.Game.utils;

@IsLogicProcess
public class ShootSingleTargetBullet extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<BulletTypeComp> bulletTypeMapper;
    ComponentMapper<PositionComp> positionMapper;

    ComponentMapper<TeamComp> teamMapper;
    ComponentMapper<ColorLevelComp> colorLevelMapper;
    ComponentMapper<StainSplashComp> stainSplashMapper;
    ComponentMapper<TargetSingleComp> targetSingleMapper;

    public ShootSingleTargetBullet(){
        super(Aspect.all(CooldownComp.class, BulletTypeComp.class, TargetSingleComp.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);

            if(targetSingle.targetId != -1){
                BulletTypeComp bulletTypeComp = bulletTypeMapper.get(entityId);

                for(int i = 0; i < cooldown.shootCount * bulletTypeComp.amt; i++){
                    var bullet = bulletTypeComp.type.create();
                    int bulletId = bullet.getId();
                    PositionComp pos = positionMapper.get(entityId);
                    PositionComp bulletPos = positionMapper.get(bullet);
                    bulletPos.x = pos.x;
                    bulletPos.y = pos.y;

                    utils.targetCompParser(entityId, bulletId);

                    utils.setTeam(bulletId, teamMapper.get(entityId).team);

                    if(colorLevelMapper.has(bullet)){
                        stainSplashMapper.get(bullet).damage = colorLevelMapper.get(entityId).level;
                    }
                }
            }
        }
    }
}
