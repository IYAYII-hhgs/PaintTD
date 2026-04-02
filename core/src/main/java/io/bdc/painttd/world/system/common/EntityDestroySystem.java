package io.bdc.painttd.world.system.common;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

/**
 * Drains queued destroy requests during the recycle phase.
 * <p>
 * Any dependency-ordered teardown must be completed before an entity reaches this system.
 * Once a destroy request enters {@code EntityDestroySystem}, cleanup must remain order-independent.
 */
public class EntityDestroySystem extends WorldSystem {
    public DestroyRequestQueue destroyQueue;
    public EntityAssembler entityAssembler;

    public EntityDestroySystem(WorldRuntime world, WorldPhase phase, int order, EntityAssembler entityAssembler) {
        super(world, phase, order);
        this.entityAssembler = entityAssembler;
    }

    @Override
    public void onStoreBind(WorldStoreBinder binder) {
        destroyQueue = binder.require(DestroyRequestQueue.class);
    }

    @Override
    public void run(float delta) {
        for (int i = 0; i < destroyQueue.size(); i++) {
            int eid = destroyQueue.eids.get(i);
            entityAssembler.destroy(world, eid);
            world.idManager.free(eid);
        }
        destroyQueue.clear();
    }
}
