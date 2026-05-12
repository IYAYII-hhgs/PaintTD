package io.bdc.painttd.world.family.bullet;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;

/**
 * 子弹生命周期相关 API
 * 子弹碰撞耐久记忆了上次碰撞时间，只在需要时检查即可。
 * 这里也提供了耐久控制入口。
 * 子弹生存时长则初始化后流程自动控制，一般无需手动管理。
 */

public class BulletLifetimeAPI implements WorldAPI {
    BulletLifetimeStore store;
    DestroyQueue destroyQueue;
    WorldTimeAPI timeAPI;

    @Override
    public void onBind(WorldAccess binder) {
        store = binder.getStore(BulletLifetimeStore.class);
        destroyQueue = binder.getStore(DestroyQueue.class);
        timeAPI = binder.getApi(WorldTimeAPI.class);
    }

    // Hit CD
    public boolean inHitCooldown(int eid) {
        int d = timeAPI.sinceTick(store.lastHitTickArray.get(store.slotOf(eid)));
        return d > 0 && d < BulletLifetimeStore.HIT_COOLDOWN;
    }

    public void resetCooldown(int eid) {
        store.lastHitTickArray.set(store.slotOf(eid), timeAPI.getTick());
    }

    // Duration
    public float getDuration(int eid) {
        return store.durationArray.get(store.slotOf(eid));
    }

    public float countdownDuration(int eid) {
        int slot = store.slotOf(eid);
        float duration = store.durationArray.get(slot);
        store.durationArray.set(slot, --duration);
        return duration;
    }

    public void setDuration(int eid, float duration) {
        store.durationArray.set(store.slotOf(eid), duration);
    }

    // 回收子弹
    public void recycle(int eid) {
        destroyQueue.add(eid);
    }

    // 一站式入口
    public boolean tryConsume(int eid) {
        if (getDuration(eid) <= 0) {
            recycle(eid);
            return false;
        }
        if (inHitCooldown(eid)) {
            return false;
        }
        resetCooldown(eid);
        float newDuration = countdownDuration(eid);
        if (newDuration <= 0) {
            recycle(eid);
        }
        return true;
    }
}
