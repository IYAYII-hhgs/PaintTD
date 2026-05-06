package io.bdc.painttd.world.family.turret;

import io.bdc.painttd.world.family.*;

public class TurretDef implements ModuleDef {
    public float cooldown;
    public float range;
    public TargetPriority targetPriority = TargetPriority.ENTITY_FIRST;

    public enum TargetPriority {
        ENTITY_FIRST,
        CELL_ONLY
    }

    public TurretDef(float cooldown, float range) {
        this.cooldown = cooldown;
        this.range = range;
    }

    public TurretDef setTargetPriority(TargetPriority targetPriority) {
        this.targetPriority = targetPriority;
        return this;
    }
}
