package io.blackdeluxecat.painttd.game.content.components;

public class HealthComp extends CloneableComponent{
    public float health, maxHealth;

    public HealthComp(){}

    public HealthComp(float maxHealth){
        this.maxHealth = maxHealth;
    }

    @Override
    public HealthComp copy(){
        return new HealthComp(maxHealth);
    }

    @Override
    protected void reset(){
        maxHealth = 0;
        health = 0;
    }
}
