package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class CooldownComp extends CopyableComponent{
    public float cooldown;
    public float currentCooldown;
    public int shootCount;

    public CooldownComp(){}

    public CooldownComp(float cooldownTicks){
        this.cooldown = cooldownTicks;
        this.currentCooldown = cooldownTicks;
    }

    @Override
    public CooldownComp copy(CopyableComponent other){
        CooldownComp c = (CooldownComp)other;
        cooldown = c.cooldown;
        currentCooldown = c.currentCooldown;
        shootCount = c.shootCount;
        return this;
    }

    @Override
    protected void reset(){
        currentCooldown = 0;
    }
}
