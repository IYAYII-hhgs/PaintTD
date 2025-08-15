package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

/**
 * 带有冷却的武器的攻击
 * */
public class CooldownShoot extends IteratingSystem{
    public ComponentMapper<CooldownComp> cm;
    public ComponentMapper<DamageComp> dm;
    public ComponentMapper<TargetComp> tm;
    public ComponentMapper<DamageReceiveComp> drc;

    public CooldownShoot(){
        super(Aspect.all(TargetComp.class, CooldownComp.class, DamageComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        cm = world.getMapper(CooldownComp.class);
        dm = world.getMapper(DamageComp.class);
        tm = world.getMapper(TargetComp.class);
        drc = world.getMapper(DamageReceiveComp.class);
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cm.get(entityId);
        if(cooldown.currentCooldown <= 0){
            cooldown.currentCooldown = cooldown.cooldown;
            int tgt = tm.get(entityId).targetId;
            if(tgt != -1){
                drc.get(tgt).add(dm.get(entityId).damage);
            }
        }else{
            cooldown.currentCooldown -= 1;
        }
    }
}