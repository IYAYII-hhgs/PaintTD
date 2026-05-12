package io.bdc.painttd.world.family.weapon;

import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.family.turret.*;
import io.bdc.painttd.world.store.request.*;

public class BulletWeaponDef extends WeaponDef {
    public float hitDuration = 1;
    public float directDamage = 0.1f;
    public float slashDamage = 0f;
    public float slashRadius = 0f;
    public float cellSplashDamage = 0f;
    public float cellSlashRadius = 0f;

    public EntityDef bulletDef;

    public BulletWeaponDef(EntityDef bulletDef) {
        this.bulletDef = bulletDef;
    }

    @Override
    public boolean onAssemble(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var dependent = binder.getStore(TurretStore.class);
        if (!dependent.has(eid)) throw new IllegalStateException("TurretStore is missed for" + req.entityDef.name);

        var store = binder.getStore(BulletWeaponStore.class);
        store.createAndSet(eid, this);
        return true;
    }

    public BulletWeaponDef hitDuration(float amt) {
        this.hitDuration = amt;
        return this;
    }

    public BulletWeaponDef directDamage(float amt) {
        this.directDamage = amt;
        return this;
    }

    public BulletWeaponDef slashDamage(float amt, float radius) {
        this.slashDamage = amt;
        this.slashRadius = radius;
        return this;
    }

    public BulletWeaponDef cellSlashDamage(float amt, float radius) {
        this.cellSplashDamage = amt;
        this.cellSlashRadius = radius;
        return this;
    }
}
