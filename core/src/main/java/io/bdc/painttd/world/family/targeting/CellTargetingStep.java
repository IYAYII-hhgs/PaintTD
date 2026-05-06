package io.bdc.painttd.world.family.targeting;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.request.*;

public class CellTargetingStep implements AssembleStep {
    public CellTargetingStore.CellStrategy strategy;

    public CellTargetingStep setup(CellTargetingStore.CellStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        binder.getStore(CellTargetingStore.class).createAndSet(eid, strategy);
    }
}
