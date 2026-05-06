package io.bdc.painttd.world.family.weapon;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.system.*;

public class WeaponActSystem extends WorldSystem {
    WeaponStore store;

    public WeaponActSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        store = binder.getStore(WeaponStore.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < store.size(); slot++) {
            int eid = store.eidOf(slot);
            if (eid == -1) continue;
            var act = store.actArray.get(slot);
            if (act == null) continue;
            act.act(eid);
        }
    }
}
