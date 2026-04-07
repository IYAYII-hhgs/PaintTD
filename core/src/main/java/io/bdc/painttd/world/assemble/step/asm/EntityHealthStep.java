package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.store.request.*;

public final class EntityHealthStep implements AssembleStep {
    public static final String TYPE = "EntityHealthStep";

    public float hp = 1, maxHp = 1;

    public EntityHealthStep setup(float hp, float maxHp) {
        this.hp = hp;
        this.maxHp = maxHp;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var store = binder.getStore(EntityHealthStore.class);
        store.create(eid, hp, maxHp);
    }
}
