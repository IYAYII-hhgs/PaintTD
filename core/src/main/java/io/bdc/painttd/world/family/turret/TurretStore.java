package io.bdc.painttd.world.family.turret;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.store.*;

public class TurretStore extends ArrayEntityStoreBase {
    public Array<TurretDef> defArray = new Array<>();
    public FloatArray cdArray = new FloatArray();

    public boolean createAndSet(int eid, TurretDef def) {
        boolean created = createRow(eid);
        int slot = slotOf(eid);
        defArray.set(slot, def);
        cdArray.set(slot, def.cooldown);
        return created;
    }

    @Override
    protected void onRowCreated(int eid, int slot) {
        defArray.add(null);
        cdArray.add(0f);
    }

    @Override
    protected void onRowRemoved(int removedEid, int removedSlot, int movedEid, int movedFromSlot) {
        if (movedEid != NO_EID) {
            defArray.swap(movedFromSlot, removedSlot);
            cdArray.swap(movedFromSlot, removedSlot);
        }
        defArray.pop();
        cdArray.pop();
    }

    @Override
    protected void onRowsCleared() {
        defArray.clear();
        cdArray.clear();
    }
}
