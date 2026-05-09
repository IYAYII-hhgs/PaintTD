package io.bdc.painttd.world.family.turret;

import io.bdc.painttd.world.family.*;

public class TurretDef implements ModuleDef {
    public float cooldown;
    public float range;

    public TurretDef(float cooldown, float range) {
        this.cooldown = cooldown;
        this.range = range;
    }
}
