package io.bdc.painttd.world.api;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.act.*;

public class ActTypeAPI implements WorldAPI {
    public WorldAccess binder;
    //首版先存直接引用, 不做typeId映射
    public ObjectMap<Act, Act> runtimeActCache = new ObjectMap<>();

    @Override
    public void onBind(WorldAccess binder) {
        this.binder = binder;
    }

    public <T extends Act> T getOrCreate(T prototype) {
        T act = (T) runtimeActCache.get(prototype);
        if (act == null) {
            act = (T)prototype.runtimeInstance();
            runtimeActCache.put(prototype, act);
            act.onBind(binder);
        }

        return act;
    }
}
