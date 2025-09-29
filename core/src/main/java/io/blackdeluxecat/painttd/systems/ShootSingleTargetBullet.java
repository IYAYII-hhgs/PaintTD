package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;

@IsLogicProcess
public class ShootSingleTargetBullet extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<BulletTypeComp> bulletTypeMapper;
    ComponentMapper<PositionComp> positionMapper;

    ComponentMapper<TeamComp> teamMapper;
    ComponentMapper<ColorLevelComp> colorLevelMapper;
    ComponentMapper<StainSlashComp> stainSlashMapper;
    ComponentMapper<TargetSingleComp> targetSingleMapper;

    public ShootSingleTargetBullet(){
        super(Aspect.all(CooldownComp.class, BulletTypeComp.class, TargetSingleComp.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            BulletTypeComp bulletTypeComp = bulletTypeMapper.get(entityId);

            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                Gdx.app.log("", bulletTypeComp.amt + ", " + cooldown.shootCount);
                for(int i = 0; i < cooldown.shootCount * bulletTypeComp.amt; i++){
                    var bullet = bulletTypeComp.type.create();
                    Gdx.app.log("", "b" + bullet.getId());
                    PositionComp pos = positionMapper.get(entityId);
                    positionMapper.get(bullet).x = pos.x;
                    positionMapper.get(bullet).y = pos.y;

                    if(targetSingleMapper.has(bullet)){
                        targetSingleMapper.get(bullet).targetId = targetSingle.targetId;
                    }
                    if(teamMapper.has(bullet)){
                        teamMapper.get(bullet).team = teamMapper.get(entityId).team;
                    }

                    if(colorLevelMapper.has(bullet)){
                        stainSlashMapper.get(bullet).level = colorLevelMapper.get(entityId).level;
                    }
                }
            }
        }
    }
}
