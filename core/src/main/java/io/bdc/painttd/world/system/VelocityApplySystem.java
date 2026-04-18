package io.bdc.painttd.world.system;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;

public class VelocityApplySystem extends WorldSystem {
    VelocityStore velStore;
    TransformStore transStore;
    TransformAPI transformAPI;

    public VelocityApplySystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        velStore = binder.getStore(VelocityStore.class);
        transStore = binder.getStore(TransformStore.class);
        transformAPI = binder.getApi(TransformAPI.class);
    }

    @Override
    public void run(float delta) {
        float[] velocityXItems = velStore.x.items;
        float[] velocityYItems = velStore.y.items;
        float[] transformXItems = transStore.x.items;
        float[] transformYItems = transStore.y.items;

        for (int velocitySlot = 0; velocitySlot < velStore.size(); velocitySlot++) {
            float vx = velocityXItems[velocitySlot];
            float vy = velocityYItems[velocitySlot];
            if (vx == 0f && vy == 0f) {
                continue;
            }

            int eid = velStore.eidOf(velocitySlot);
            int transformSlot = transStore.slotOf(eid);
            if (transformSlot < 0) {
                continue;
            }

            transformXItems[transformSlot] += vx;
            transformYItems[transformSlot] += vy;
            transformAPI.markDirty(eid);
        }
    }
}
