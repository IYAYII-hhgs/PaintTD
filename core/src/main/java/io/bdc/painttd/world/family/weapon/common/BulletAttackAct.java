package io.bdc.painttd.world.family.weapon.common;

import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.act.*;
import io.bdc.painttd.world.assemble.step.post.*;
import io.bdc.painttd.world.family.bullet.*;
import io.bdc.painttd.world.family.targeting.*;
import io.bdc.painttd.world.family.turret.*;
import io.bdc.painttd.world.family.weapon.*;
import io.bdc.painttd.world.store.*;

/**
 * 生成的是通用子弹，重写{@code getTargetAndSpawnBullet}方法给子弹装配添加PostSteps，以增加伤害等属性
 * {@code act}提供了默认的射击流程
 */

public class BulletAttackAct implements WeaponAct {
    public EntityDef bulletDef;//TODO def放这里的设计存疑，更适合的可能是WeaponDef

    WeaponStore weaponStore;
    TurretStore turretStore;
    EntityTargetingStore entityTargetingStore;
    PositionStore positionStore;
    CellTargetingStore cellTargetingStore;
    SpawnRequestQueue spawnRequestQueue;
    WorldAccess binder;

    public BulletAttackAct setBulletDef(EntityDef bulletDef) {
        this.bulletDef = bulletDef;
        return this;
    }

    @Override
    public void act(int eid) {
        int turretSlot = turretStore.slotOf(eid);
        if (turretSlot == -1) return;
        float cd = turretStore.cdArray.get(turretSlot);
        if (cd > 0) return;

        int weaponSlot = weaponStore.slotOf(eid);
        WeaponDef weaponDef = weaponStore.defArray.get(weaponSlot);
        TurretDef turretDef = turretStore.defArray.get(turretSlot);

        int targetEid = -1;
        int targetCell = -1;

        if (entityTargetingStore != null) {
            int etSlot = entityTargetingStore.slotOf(eid);
            if (etSlot != -1) targetEid = entityTargetingStore.targetIdArray.get(etSlot);
        }
        if (cellTargetingStore != null) {
            int ctSlot = cellTargetingStore.slotOf(eid);
            if (ctSlot != -1) targetCell = cellTargetingStore.targetCellArray.get(ctSlot);
        }

        boolean acted = spawnBullet(eid, targetEid, targetCell, turretDef, weaponDef);

        if (acted) {
            turretStore.cdArray.set(turretSlot, turretDef.cooldown);
        }
    }

    public boolean spawnBullet(int eid, int targetEid, int targetCell, TurretDef turretDef, WeaponDef weaponDef) {
        boolean acted = false;
        if (targetEid != -1) {
            acted = spawnBulletTargetEntity(eid, targetEid, turretDef, weaponDef);
        } else if (targetCell != -1) {
            acted = spawnBulletTargetCell(eid, targetCell, turretDef, weaponDef);
        }
        return acted;
    }

    public boolean spawnBulletTargetEntity(int eid, int tgteid, TurretDef turretDef, WeaponDef weaponDef) {
        var req = SpawnRequestQueue.obtain();
        int posSlot = positionStore.slotOf(eid);
        float x = positionStore.x.get(posSlot);
        float y = positionStore.y.get(posSlot);
        req.setup(bulletDef, x, y);
        req.addStep(new PostBulletStep().setup(eid, tgteid).damage(0.1f));
        req.addStep(new PostTeamStep().setup(0));
        spawnRequestQueue.add(req);
        return true;
    }

    public boolean spawnBulletTargetCell(int eid, int tgtcid, TurretDef turretDef, WeaponDef weaponDef) {
        return false;
    }

    @Override
    public Act runtimeInstance() {
        var d = new BulletAttackAct();
        d.bulletDef = bulletDef;
        return d;
    }

    @Override
    public void onBind(WorldAccess binder) {
        this.binder = binder;
        weaponStore = binder.getStore(WeaponStore.class);
        turretStore = binder.getStore(TurretStore.class);
        entityTargetingStore = binder.getStore(EntityTargetingStore.class);
        cellTargetingStore = binder.getStore(CellTargetingStore.class);
        spawnRequestQueue = binder.getStore(SpawnRequestQueue.class);
        positionStore = binder.getStore(PositionStore.class);
    }
}
