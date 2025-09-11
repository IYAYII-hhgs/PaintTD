package io.blackdeluxecat.painttd.content.components.logic;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.*;

public class HealthComp extends CopyableComponent implements Json.Serializable{
    /**
     * 无最大生命值限制令maxHealth == -1
     */
    public float health, maxHealth;

    public HealthComp(){
    }

    public HealthComp(float health, float maxHealth){
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public HealthComp(float maxHealth){
        this.maxHealth = maxHealth;
        this.health = maxHealth == -1 ? 0 : maxHealth;
    }

    @Override
    public HealthComp copy(CopyableComponent other){
        HealthComp healthComp = (HealthComp)other;
        health = healthComp.health;
        maxHealth = healthComp.maxHealth;
        return this;
    }

    @Override
    protected void reset(){
        health = 0;
        maxHealth = 0;
    }

    @Override
    public void write(Json json){
        json.writeValue("health", health);
    }

    @Override
    public void read(Json json, JsonValue jsonData){
        health = jsonData.getFloat("health");
    }

    @Override
    public void refill(CopyableComponent def){
        HealthComp healthComp = (HealthComp)def;
        maxHealth = healthComp.maxHealth;
    }
}
