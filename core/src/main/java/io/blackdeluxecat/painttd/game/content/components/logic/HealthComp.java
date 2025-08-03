package io.blackdeluxecat.painttd.game.content.components.logic;

public class HealthComp extends CopyableComponent{
    /**无最大生命值限制令maxHealth == -1*/
    public float health, maxHealth;

    public HealthComp(){}

    public HealthComp(float maxHealth){
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    @Override
    public HealthComp copy(CopyableComponent other){
        HealthComp healthComp = (HealthComp)other;
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
