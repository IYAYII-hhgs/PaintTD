package io.bdc.painttd.world.family.bullet;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.request.*;

import static io.bdc.painttd.world.store.ArrayEntityStoreBase.NO_EID;

public class PostBulletStep implements PostSpawnStep {
    public int source = NO_EID;
    public int targetEid = NO_EID;
    public float speed = 2f/60f;

    public PostBulletStep setup(int source, int targetEid) {
        this.source = source;
        this.targetEid = targetEid;
        return this;
    }

    public PostBulletStep speed(float speed) {
        this.speed = speed;
        return this;
    }

    @Override
    public void run(int eid, EntitySpawnRequest req, WorldAccess binder) {
        BulletFlightStore store = binder.getStore(BulletFlightStore.class);
        store.createAndSet(eid, source, targetEid);
        store.setSpeed(eid, speed);
    }
}
