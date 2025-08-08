package io.blackdeluxecat.painttd.content.components.logic;

/**武器的最终攻击力组件。*/
public class DamageComp extends CopyableComponent{
    public float damage;

    public DamageComp(){}

    public DamageComp(float damage){
        this.damage = damage;
    }

    @Override
    public DamageComp copy(CopyableComponent other){
        DamageComp damageComp = (DamageComp)other;
        this.damage = damageComp.damage;
        return this;
    }

    @Override
    protected void reset(){
        this.damage = 0;
    }
}
