package io.bdc.painttd.world.family.weapon;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.store.*;

public class BulletWeaponStore extends ArrayEntityStoreBase {
    public Array<BulletWeaponDef> defArray = new Array<>();

    public boolean createAndSet(int eid, BulletWeaponDef def) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        defArray.set(slot, def);
        return created;
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        defArray.add(null);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            defArray.swap(movedFromSlot, removedSlot);
        }
        defArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        defArray.clear();
    }
}
