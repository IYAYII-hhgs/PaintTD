package io.bdc.painttd.world.assemble.step.asm;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.store.request.*;

public final class DebugTeamStep implements AssembleStep {
    public int team = 0;

    public DebugTeamStep setup(int team) {
        this.team = team;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        var store = binder.getStore(EntityTeamStore.class);
        store.createAndSet(eid, team);
    }
}
