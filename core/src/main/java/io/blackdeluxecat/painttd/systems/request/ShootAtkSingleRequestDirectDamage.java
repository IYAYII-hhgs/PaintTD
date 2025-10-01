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
public class ShootAtkSingleRequestDirectDamage extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<DamageComp> damageMapper;

    public ShootAtkSingleRequestDirectDamage(){
        super(Aspect.all(CooldownComp.class, DamageComp.class, TargetSingleComp.class, MarkerComp.ShootAttacker.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                DamageComp damage = damageMapper.get(entityId);

                for(int i = 0; i < cooldown.shootCount; i++){
                    damageQueue.add(entityId, targetSingle.targetId, DamageQueue.newData(DamageQueue.DirectDamageData.class).dmg(damage.damage));
                }
            }
        }
    }
}
