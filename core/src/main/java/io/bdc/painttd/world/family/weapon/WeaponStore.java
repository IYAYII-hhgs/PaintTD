package io.bdc.painttd.world.family.weapon;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.store.*;

public class WeaponStore extends ArrayEntityStoreBase {
    public Array<WeaponDef> defArray = new Array<>();
    public Array<WeaponAct> actArray = new Array<>();   //已编译Act

    public boolean createAndSet(int eid, WeaponDef def, WeaponAct act) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        defArray.set(slot, def);
        actArray.set(slot, act);
        return created;
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        defArray.add(null);
        actArray.add(null);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            defArray.swap(movedFromSlot, removedSlot);
            actArray.swap(movedFromSlot, removedSlot);
        }
        defArray.pop();
        actArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        defArray.clear();
        actArray.clear();
    }
}
