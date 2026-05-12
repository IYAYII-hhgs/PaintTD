package io.bdc.painttd.world.family.bullet;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class BulletTargetPushSystem extends WorldSystem {
    VelocityStore velocityStore;
    PositionStore positionStore;
    BulletFlightStore bulletFlightStore;

    Vector2 tmp = new Vector2();

    public BulletTargetPushSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        velocityStore = binder.getStore(VelocityStore.class);
        bulletFlightStore = binder.getStore(BulletFlightStore.class);
        positionStore = binder.getStore(PositionStore.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < bulletFlightStore.size(); slot++) {
            int targetEid = bulletFlightStore.targetEntityArray.get(slot);
            var eid = bulletFlightStore.eidOf(slot);
            if (targetEid != -1) {
                // 追踪目标
                int posSlot = positionStore.slotOf(eid);
                int targetPosSlot = positionStore.slotOf(targetEid);

                float dx = positionStore.x.get(targetPosSlot) - positionStore.x.get(posSlot);
                float dy = positionStore.y.get(targetPosSlot) - positionStore.y.get(posSlot);
                float maxSpeed = bulletFlightStore.maxSpeedArray.get(slot);
                tmp.set(dx, dy).nor().scl(maxSpeed);
                velocityStore.set(eid, tmp.x, tmp.y);
            } else {
                // 惯性向前
                int velSlot = velocityStore.slotOf(eid);
                float maxSpeed = bulletFlightStore.maxSpeedArray.get(slot);
                tmp.set(velocityStore.x.get(velSlot), velocityStore.y.get(velSlot)).nor().scl(maxSpeed);
                velocityStore.set(eid, tmp.x, tmp.y);
            }
        }
    }
}
