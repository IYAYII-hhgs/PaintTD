package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.assemble.*;

public class SpawnRequestQueue implements WorldStore {
    public final Array<EntitySpawnRequest> requests = new Array<>();

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
        requests.clear();
    }
}
