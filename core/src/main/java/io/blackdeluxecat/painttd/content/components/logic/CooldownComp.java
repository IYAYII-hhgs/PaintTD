package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class CooldownComp extends CopyableComponent{
    public float cooldown;
    public float currentCooldown;

    public CooldownComp(){}

    public CooldownComp(float cooldown){
        this.cooldown = cooldown;

    }

    @Override
    public CooldownComp copy(CopyableComponent other){
        CooldownComp c = (CooldownComp)other;
        c.cooldown = cooldown;
        c.currentCooldown = currentCooldown;
        return c;
    }

    @Override
    protected void reset(){
        currentCooldown = 0;
    }
}
