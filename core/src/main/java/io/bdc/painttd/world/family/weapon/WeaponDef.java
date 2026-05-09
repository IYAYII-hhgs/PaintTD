package io.bdc.painttd.world.family.weapon;

import io.bdc.painttd.world.family.*;

public class WeaponDef implements ModuleDef {
    public float directDamage = 0.1f;
    public float slashDamage = 0f;
    public float slashRadius = 1f;
    public float cellSlashDamage = 1f;
    public float cellSlashRadius = 0f;
    public WeaponAct attackAct;

    public WeaponDef setup(float damage, WeaponAct attackAct) {
        this.directDamage = damage;
        this.attackAct = attackAct;
        return this;
    }
}
