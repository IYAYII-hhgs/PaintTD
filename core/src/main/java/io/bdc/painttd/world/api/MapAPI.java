package io.bdc.painttd.world.api;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

public class MapAPI implements WorldAPI {
    MapStore mapStore;

    @Override
    public void onBind(WorldAccess binder) {
        mapStore = binder.getStore(MapStore.class);
    }

    public MapStore getMapStore() {
        return mapStore;
    }

    /**
     * @return 实际造成的伤害
     */
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

    public void coverCell(float amount, int cid, int team) {
        // 尝试伤害敌对色块，如果有
        int cellTeam = mapStore.teamMask[cid];
        if (cellTeam != team) {
            amount -= damageCell(amount, cid);
        }
        // 余量生成友方色块
        if (amount > 0) {
            mapStore.hpMask[cid] += amount;
            mapStore.teamMask[cid] = team;
        }
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
