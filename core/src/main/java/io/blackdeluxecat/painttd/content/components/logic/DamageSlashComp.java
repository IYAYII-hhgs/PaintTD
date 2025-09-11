package io.blackdeluxecat.painttd.content.components.logic;

import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.*;

@Transient
public class DamageSlashComp extends CopyableComponent{
    public float damage;
    public float range;

    public DamageSlashComp(){
    }

    public DamageSlashComp(float damage, float range){
        this.damage = damage;
        this.range = range;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        DamageSlashComp otherComp = (DamageSlashComp)other;
        this.damage = otherComp.damage;
        this.range = otherComp.range;
        return this;
    }

    @Override
    protected void reset(){
    }

    @Override
    public void refill(CopyableComponent def){
        copy(def);
    }
}
