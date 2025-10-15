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
public class ShootAtkSingleFireDirectDamage extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<DamageComp> damageMapper;

    public ShootAtkSingleFireDirectDamage(){
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
                    Events.fire(EventTypes.DamageEvent.class, e -> {
                        e.source = entityId;
                        e.target = targetSingle.targetId;
                        e.damage = damage.damage;
                    });
                }
            }
        }
    }
}
