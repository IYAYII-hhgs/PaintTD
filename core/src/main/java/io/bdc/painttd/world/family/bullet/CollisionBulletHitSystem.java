package io.bdc.painttd.world.family.bullet;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class CollisionBulletHitSystem extends WorldSystem {
    EntityTeamStore teamStore;
    BulletStore bulletStore;
    CollisionBodyStore collisionBodyStore;
    CollisionDispatchAPI collisionDispatchAPI;
    DamageAPI damageAPI;

    public CollisionBulletHitSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        teamStore = binder.getStore(EntityTeamStore.class);
        bulletStore = binder.getStore(BulletStore.class);
        collisionBodyStore = binder.getStore(CollisionBodyStore.class);
        collisionDispatchAPI = binder.getApi(CollisionDispatchAPI.class);
        damageAPI = binder.getApi(DamageAPI.class);

        collisionDispatchAPI.onEe(this::onEe);
    }

    @Override
    public void run(float delta) {
    }

    public void onEe(int eidA, int eidB) {
        tryAsBullet(eidA, eidB);
        tryAsBullet(eidB, eidA);
    }

    public void tryAsBullet(int bullet, int victim) {
        int bulletSlot = bulletStore.slotOf(bullet);
        if (bulletSlot < 0) return;
        int bBodySlot = collisionBodyStore.slotOf(bullet);
        int vBodySlot = collisionBodyStore.slotOf(victim);
        if ((collisionBodyStore.categoryArray.get(bBodySlot) & collisionBodyStore.categoryMaskArray.get(vBodySlot)) == 0) return;//确认victim遮罩匹配子弹
        if (teamStore.get(bullet) == teamStore.get(victim)) return;//确认不是同队伍

        float damage = bulletStore.damageArray.get(bulletSlot);
        damageAPI.realDamage(victim, damage);
        damageAPI.realDamage(bullet, damage);
    }
}
