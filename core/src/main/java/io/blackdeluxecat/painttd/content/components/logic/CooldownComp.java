package io.blackdeluxecat.painttd.content.components.logic;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.*;

public class CooldownComp extends CopyableComponent implements Json.Serializable{
    public float cooldown;
    public float currentCooldown;
    public int shootCount;

    public CooldownComp(){
    }

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

    @Override
    public void write(Json json){
        json.writeValue("currentCooldown", currentCooldown);
    }

    @Override
    public void read(Json json, JsonValue jsonData){
        currentCooldown = jsonData.getFloat("currentCooldown");
        shootCount = 0;// looks weird
    }

    @Override
    public void refill(CopyableComponent def){
        CooldownComp cooldownComp = (CooldownComp)def;
        cooldown = cooldownComp.cooldown;
    }
}
