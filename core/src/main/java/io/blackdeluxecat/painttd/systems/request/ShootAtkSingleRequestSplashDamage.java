package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class ShootAtkSingleRequestSplashDamage extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<DamageSplashComp> damageSplashMapper;
    public ComponentMapper<PositionComp> positionMapper;

    public ShootAtkSingleRequestSplashDamage(){
        super(Aspect.all(CooldownComp.class, DamageSplashComp.class, TargetSingleComp.class, MarkerComp.ShootAttacker.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                PositionComp tgtPos = positionMapper.get(targetSingle.targetId);
                DamageSplashComp splash = damageSplashMapper.get(entityId);

                for(int i = 0; i < cooldown.shootCount; i++){
                    damageQueue.add(entityId, targetSingle.targetId, DamageQueue.newData(DamageQueue.SplashDamageData.class).pos(tgtPos.x, tgtPos.y).dmg(splash.damage, splash.range));
                }
            }
        }
    }
}
