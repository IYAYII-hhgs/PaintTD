package io.bdc.painttd.world.assemble.step.post;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.store.request.*;

import static io.bdc.painttd.world.store.ArrayEntityStoreBase.*;

public class PostTeamStep implements PostSpawnStep {
    int team;
    public PostTeamStep setup(int team) {
        this.team = team;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        EntityTeamStore store = binder.getStore(EntityTeamStore.class);
        store.set(eid, team);
    }
}
