package io.bdc.painttd.world.family.weapon;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.post.*;
import io.bdc.painttd.world.family.bullet.*;
import io.bdc.painttd.world.family.targeting.*;
import io.bdc.painttd.world.family.turret.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class BulletWeaponAttackSystem extends WorldSystem {
    BulletWeaponStore bulletWeaponStore;
    TurretStore turretStore;
    EntityTeamStore teamStore;
    EntityTargetingStore entityTargetingStore;
    PositionStore positionStore;
    CellTargetingStore cellTargetingStore;
    SpawnRequestQueue spawnRequestQueue;

    public BulletWeaponAttackSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        this.bulletWeaponStore = binder.getStore(BulletWeaponStore.class);
        this.turretStore = binder.getStore(TurretStore.class);
        this.teamStore = binder.getStore(EntityTeamStore.class);
        this.entityTargetingStore = binder.getStore(EntityTargetingStore.class);
        this.positionStore = binder.getStore(PositionStore.class);
        this.cellTargetingStore = binder.getStore(CellTargetingStore.class);
        this.spawnRequestQueue = binder.getStore(SpawnRequestQueue.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < bulletWeaponStore.size(); slot++) {
            int eid = bulletWeaponStore.eidOf(slot);
            // 检查炮台身份和炮台cd
            int turretSlot = turretStore.slotOf(eid);
            if (turretSlot == -1) continue;
            float cd = turretStore.cdArray.get(turretSlot);
            if (cd > 0) continue;


            int weaponSlot = bulletWeaponStore.slotOf(eid);
            BulletWeaponDef weaponDef = bulletWeaponStore.defArray.get(weaponSlot);
            TurretDef turretDef = turretStore.defArray.get(turretSlot);

            //获得目标
            int targetEid = -1;
            int targetCell = -1;

            int etSlot = entityTargetingStore.slotOf(eid);
            if (etSlot != -1) targetEid = entityTargetingStore.targetIdArray.get(etSlot);

            int ctSlot = cellTargetingStore.slotOf(eid);
            if (ctSlot != -1) targetCell = cellTargetingStore.targetCellArray.get(ctSlot);

            // 生成子弹
            boolean acted = spawnBullet(eid, targetEid, targetCell, turretDef, weaponDef);

            // 更新cd
            if (acted) {
                turretStore.cdArray.set(turretSlot, turretDef.cooldown);
            }
        }
    }

    public boolean spawnBullet(int eid, int targetEid, int targetCell, TurretDef turretDef, BulletWeaponDef weaponDef) {
        boolean acted = false;
        if (targetEid != -1) {
            acted = spawnBulletTargetEntity(eid, targetEid, turretDef, weaponDef);
        } else if (targetCell != -1) {
            acted = spawnBulletTargetCell(eid, targetCell, turretDef, weaponDef);
        }
        return acted;
    }

    public boolean spawnBulletTargetEntity(int eid, int tgteid, TurretDef turretDef, BulletWeaponDef weaponDef) {
        var req = SpawnRequestQueue.obtain();
        int posSlot = positionStore.slotOf(eid);
        float x = positionStore.x.get(posSlot);
        float y = positionStore.y.get(posSlot);
        req.setup(weaponDef.bulletDef, x, y);
        req.addStep(new PostBulletStep().setup(eid, tgteid).damage(weaponDef.directDamage));
        req.addStep(new PostTeamStep().setup(teamStore.get(eid)));
        spawnRequestQueue.add(req);
        return true;
    }

    public boolean spawnBulletTargetCell(int eid, int tgtcid, TurretDef turretDef, WeaponDef weaponDef) {
        // TODO
        return false;
    }
}
