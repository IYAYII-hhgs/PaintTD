package io.bdc.painttd.world.system.targeting;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.family.bullet.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class BulletTargetPushSystem extends WorldSystem {
    VelocityStore velocityStore;
    PositionStore positionStore;
    BulletStore bulletStore;

    Vector2 tmp = new Vector2();

    public BulletTargetPushSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        velocityStore = binder.getStore(VelocityStore.class);
        bulletStore = binder.getStore(BulletStore.class);
        positionStore = binder.getStore(PositionStore.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < bulletStore.size(); slot++) {
            int targetEid = bulletStore.targetEntityArray.get(slot);
            if (targetEid == -1) continue;
            var eid = bulletStore.eidOf(slot);
            int posSlot = positionStore.slotOf(eid);
            int targetPosSlot = positionStore.slotOf(targetEid);

            float dx = positionStore.x.get(targetPosSlot) - positionStore.x.get(posSlot);
            float dy = positionStore.y.get(targetPosSlot) - positionStore.y.get(posSlot);
            float maxSpeed = bulletStore.maxSpeedArray.get(slot);
            tmp.set(dx, dy).nor().scl(maxSpeed);
            velocityStore.set(eid, tmp.x, tmp.y);
        }
    }
}
