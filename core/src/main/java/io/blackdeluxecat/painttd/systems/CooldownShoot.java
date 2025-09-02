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

    public CooldownShoot(){
        super(Aspect.all(CooldownComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        cm = world.getMapper(CooldownComp.class);
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cm.get(entityId);
        cooldown.shootCount = 0;
        cooldown.currentCooldown -= 1;
        while(cooldown.currentCooldown <= 0){
            cooldown.currentCooldown += cooldown.cooldown;
            cooldown.shootCount += 1;
        }
    }
}