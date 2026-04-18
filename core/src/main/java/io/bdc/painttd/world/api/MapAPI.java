package io.bdc.painttd.world.api;

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
            mapStore.hpMask[cid] -= damage;
            if (mapStore.hpMask[cid] < 0) {
                mapStore.hpMask[cid] = 0;
                mapStore.teamMask[cid] = -1;
                return mapHp;
            }
            return damage;
        }
        return 0;
    }

    public float getCellHp(int cid) {
        return mapStore.hpMask[cid];
    }

    public void setCellHp(float hp, int cid) {
        mapStore.hpMask[cid] = hp;
    }

    public void setCellTeam(int team, int cid) {
        mapStore.teamMask[cid] = team;
    }

    public void addCellHp(float hp, int cid) {
        mapStore.hpMask[cid] += hp;
    }
}
