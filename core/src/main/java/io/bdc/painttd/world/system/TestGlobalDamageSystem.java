package io.bdc.painttd.world.system;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;

public class TestGlobalDamageSystem extends WorldSystem {
    EntityHealthStore store;
    DamageAPI api;

    public TestGlobalDamageSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        super.onBind(binder);
        store = binder.getStore(EntityHealthStore.class);
        api = binder.getApi(DamageAPI.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < store.size(); slot++) {
            api.realDamage(store.eidOf(slot), MathUtils.random(0f, 0.005f));
        }
    }
}
