package io.bdc.painttd.world.system;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

public class VelocityDecaySystem extends WorldSystem{
    VelocityStore velStore;

    Vector2 tmp1 = new Vector2();

    public VelocityDecaySystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        velStore = binder.getStore(VelocityStore.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < velStore.size(); slot++) {
            int eid = velStore.eidOf(slot);
            if (eid != -1) {
                var vel = velStore.get(eid, tmp1);
                vel.scl(0.98f);
                if (vel.len2() < 0.001f) vel.setZero();
                velStore.put(eid, vel);
            }
        }
    }
}
