package io.blackdeluxecat.painttd.game.content.components;

public class HealthComp extends CopyableComponent{
    public float health, maxHealth;

    public HealthComp(){}

    public HealthComp(float maxHealth){
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public HealthComp(float health, float maxHealth){
        this.health = health;
        this.maxHealth = maxHealth;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        HealthComp healthComp = (HealthComp) other;
        healthComp.health = health;
        healthComp.maxHealth = maxHealth;
        return healthComp;
    }

    @Override
    protected void reset(){
        health = 0;
        maxHealth = 0;
    }
}
