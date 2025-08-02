package io.blackdeluxecat.painttd.game.content.components;

public class DamageComp extends CopyableComponent{
    public float damage;

    public DamageComp(){}

    public DamageComp(float damage){
        this.damage = damage;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        DamageComp damageComp = (DamageComp)other;
        this.damage = damageComp.damage;
        return this;
    }

    @Override
    protected void reset(){
        this.damage = 0;
    }
}
