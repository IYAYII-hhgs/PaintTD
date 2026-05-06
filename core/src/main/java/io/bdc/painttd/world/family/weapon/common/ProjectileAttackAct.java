package io.bdc.painttd.world.family.weapon.common;

import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.act.*;
import io.bdc.painttd.world.family.targeting.*;
import io.bdc.painttd.world.family.turret.*;
import io.bdc.painttd.world.family.weapon.*;

public class ProjectileAttackAct implements WeaponAct {
    public EntityDef projectileDef;

    WeaponStore weaponStore;
    TurretStore turretStore;
    EntityTargetingStore entityTargetingStore;
    CellTargetingStore cellTargetingStore;

    @Override
    public Act runtimeInstance() {
        var instance = new ProjectileAttackAct();
        instance.setProjectileDef(projectileDef);
        return instance;
    }

    public ProjectileAttackAct setProjectileDef(EntityDef projectileDef) {
        this.projectileDef = projectileDef;
        return this;
    }

    @Override
    public void act(int eid) {

    }

    @Override
    public void onBind(WorldAccess binder) {
        weaponStore = binder.getStore(WeaponStore.class);
        turretStore = binder.getStore(TurretStore.class);
        entityTargetingStore = binder.getStore(EntityTargetingStore.class);
        cellTargetingStore = binder.getStore(CellTargetingStore.class);
    }
}
