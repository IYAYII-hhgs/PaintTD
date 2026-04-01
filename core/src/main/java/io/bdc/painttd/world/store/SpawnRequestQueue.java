package io.bdc.painttd.world.store;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.def.*;

public class SpawnRequestQueue implements WorldStore {
    public final Array<EntityDef> entityDefs = new Array<>();
    public final IntArray eids = new IntArray();
    public final FloatArray x = new FloatArray();
    public final FloatArray y = new FloatArray();

    public int size() {
        return entityDefs.size;
    }

    public void add(EntityDef entityDef, int eid, float xValue, float yValue) {
        entityDefs.add(entityDef);
        eids.add(eid);
        x.add(xValue);
        y.add(yValue);
    }

    public void clear() {
        entityDefs.clear();
        eids.clear();
        x.clear();
        y.clear();
    }
}
