package io.bdc.painttd.world.family.weapon.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.act.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.family.targeting.*;
import io.bdc.painttd.world.family.turret.*;
import io.bdc.painttd.world.family.weapon.*;

public class LaserAttackAct implements WeaponAct {
    DamageAPI damageAPI;
    WeaponStore weaponStore;
    TurretStore turretStore;
    EntityTargetingStore entityTargetingStore;
    CellTargetingStore cellTargetingStore;

    @Override
    public Act runtimeInstance() {
        return new LaserAttackAct();
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

        float dmg = weaponDef.damage;
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

        boolean acted = false;
        switch (turretDef.targetPriority) {
            case ENTITY_FIRST:
                if (targetEid != -1) {
                    damageAPI.realDamage(targetEid, dmg);
                    acted = true;
                } else if (targetCell != -1) {
                    //TODO cell damage
                    acted = false;
                }
                break;
            case CELL_ONLY:
                if (targetCell != -1) {
                    acted = false;
                }
                break;
        }

        if (acted) {
            turretStore.cdArray.set(turretSlot, turretDef.cooldown);
        }
    }

    @Override
    public void onBind(WorldAccess binder) {
        damageAPI = binder.getApi(DamageAPI.class);
        weaponStore = binder.getStore(WeaponStore.class);
        turretStore = binder.getStore(TurretStore.class);
        entityTargetingStore = binder.optionalStore(EntityTargetingStore.class);
        cellTargetingStore = binder.optionalStore(CellTargetingStore.class);
    }
}
