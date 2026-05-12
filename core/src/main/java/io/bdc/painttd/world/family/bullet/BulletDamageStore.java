package io.bdc.painttd.world.family.bullet;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.family.weapon.*;
import io.bdc.painttd.world.store.*;

public class BulletDamageStore extends ArrayEntityStoreBase {
    public FloatArray directDamageArray = new FloatArray();
    public FloatArray splashDamageArray = new FloatArray();
    public FloatArray splashRadiusArray = new FloatArray();
    public FloatArray cellSplashDamageArray = new FloatArray();
    public FloatArray cellSplashRadiusArray = new FloatArray();

    public boolean createAndSet(int eid, BulletWeaponDef def) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        directDamageArray.set(slot, def.directDamage);
        splashDamageArray.set(slot, def.slashDamage);
        splashRadiusArray.set(slot, def.slashRadius);
        cellSplashDamageArray.set(slot, def.cellSplashDamage);
        cellSplashRadiusArray.set(slot, def.cellSlashRadius);
        return created;
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        directDamageArray.add(0f);
        splashDamageArray.add(0f);
        splashRadiusArray.add(0f);
        cellSplashDamageArray.add(0f);
        cellSplashRadiusArray.add(0f);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            directDamageArray.swap(movedFromSlot, removedSlot);
            splashDamageArray.swap(movedFromSlot, removedSlot);
            splashRadiusArray.swap(movedFromSlot, removedSlot);
            cellSplashDamageArray.swap(movedFromSlot, removedSlot);
            cellSplashRadiusArray.swap(movedFromSlot, removedSlot);
        }
        directDamageArray.pop();
        splashDamageArray.pop();
        splashRadiusArray.pop();
        cellSplashDamageArray.pop();
        cellSplashRadiusArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        directDamageArray.clear();
        splashDamageArray.clear();
        splashRadiusArray.clear();
        cellSplashDamageArray.clear();
        cellSplashRadiusArray.clear();
    }
}
