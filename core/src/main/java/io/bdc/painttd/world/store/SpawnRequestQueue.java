package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.store.request.*;

public class SpawnRequestQueue implements WorldStore {
    public final Array<EntitySpawnRequest> requests = new Array<>();

    public int lastSpawn;

    public int size() {
        return requests.size;
    }

    public void add(EntitySpawnRequest request) {
        requests.add(request);
    }

    public void clear() {
        for (var req : requests) {
            req.reset();
        }
        lastSpawn = size();
        requests.clear();
    }

    public static Pool<EntitySpawnRequest> pool = new Pool<>(8, 4096) {
        @Override
        protected EntitySpawnRequest newObject() {
            return new EntitySpawnRequest();
        }
    };

    public static EntitySpawnRequest obtain() {
        return pool.obtain();
    }

}
