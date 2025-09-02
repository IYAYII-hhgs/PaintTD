package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.game.request.*;

import static io.blackdeluxecat.painttd.game.Game.damageQueue;

public class ShootSingleRequestDamage extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<DamageComp> damageMapper;

    public ShootSingleRequestDamage() {
        super(Aspect.all(CooldownComp.class, DamageComp.class, TargetSingleComp.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        cooldownMapper = world.getMapper(CooldownComp.class);
        targetSingleMapper = world.getMapper(TargetSingleComp.class);
        damageMapper = world.getMapper(DamageComp.class);
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                DamageComp damage = damageMapper.get(entityId);
                for(int i = 0; i < cooldown.shootCount; i++){
                    damageQueue.add(entityId, targetSingle.targetId, damage.damage, DamageQueue.DamageRequestType.direct);
                }
            }
        }
    }
}
