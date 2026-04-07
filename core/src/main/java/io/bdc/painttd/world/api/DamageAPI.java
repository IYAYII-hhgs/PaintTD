package io.bdc.painttd.world.api;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

public class DamageAPI implements WorldAPI {
    public WorldRuntime world;
    public EntityHealthStore store;

    public DamageAPI() {
    }

    @Override
    public void onBind(WorldAccess binder) {
        world = binder.world;
        store = binder.getStore(EntityHealthStore.class);
    }

    /** 实际应用伤害 */
    protected void applyDamage(int eid, float damage) {
        int slot = store.slotOf(eid);
        if (slot != -1) {
            float hp = store.healths.get(slot);
            hp -= damage;
            store.healths.set(slot, hp);
        }
    }

    public void realDamage(int eid, float damage) {
        applyDamage(eid, damage);
    }
}
