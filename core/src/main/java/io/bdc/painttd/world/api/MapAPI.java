package io.bdc.painttd.world.api;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

public class MapAPI implements WorldAPI {
    MapStore mapStore;

    @Override
    public void onBind(WorldAccess binder) {
        mapStore = binder.getStore(MapStore.class);
    }

    public float damageCell(float damage, int cid) {
        float mapHp = mapStore.hpMask[cid];
        if (mapHp > 0) {
            float realDamage = Math.min(mapHp, damage);
            mapStore.hpMask[cid] -= realDamage;
            return realDamage;
        }
        return 0;
    }

    public void setCellHp(float hp, int cid) {
        mapStore.hpMask[cid] = hp;
    }

    public void addCellHp(float hp, int cid) {
        mapStore.hpMask[cid] += hp;
    }
}
