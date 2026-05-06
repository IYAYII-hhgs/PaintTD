package io.bdc.painttd.world.family.weapon;

import io.bdc.painttd.world.family.*;

public class WeaponDef implements ModuleDef {
    public float damage = 0.1f;
    public WeaponAct attackAct;

    public WeaponDef setup(float damage, WeaponAct attackAct) {
        this.damage = damage;
        this.attackAct = attackAct;
        return this;
    }
}
