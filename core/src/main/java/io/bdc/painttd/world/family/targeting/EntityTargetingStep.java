package io.bdc.painttd.world.family.targeting;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.request.*;

public class EntityTargetingStep implements AssembleStep {
    public EntityTargetingStore.Targeting strategy;

    public EntityTargetingStep setup(EntityTargetingStore.Targeting strategy) {
        this.strategy = strategy;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        binder.getStore(EntityTargetingStore.class).createAndSet(eid, strategy);
    }
}
