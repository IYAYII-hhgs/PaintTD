package io.bdc.painttd.world.system;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

public class VelocityApplySystem extends WorldSystem{
    VelocityStore velStore;
    TransformStore transStore;

    Vector2 tmp1 = new Vector2(), tmp2 = new Vector2();

    public VelocityApplySystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        velStore = binder.getStore(VelocityStore.class);
        transStore = binder.getStore(TransformStore.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < velStore.size(); slot++) {
            int eid = velStore.eidOf(slot);
            if (eid != -1) {
                var vel = velStore.get(eid, tmp1);
                var trans = transStore.get(eid, tmp2);
                trans.add(vel.x * 1, vel.y * 1);
                transStore.set(eid, trans.x, trans.y);
            }
        }
    }
}
