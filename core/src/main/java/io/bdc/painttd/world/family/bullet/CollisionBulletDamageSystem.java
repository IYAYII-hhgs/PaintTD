package io.bdc.painttd.world.family.bullet;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class CollisionBulletDamageSystem extends WorldSystem {
    PositionStore positionStore;
    BulletDamageStore bulletDamageStore;
    BulletLifetimeAPI bulletLifetimeAPI;
    CollisionDispatchAPI dispatchAPI;
    TileBucketQueryAPI aoeAPI;
    DamageAPI damageAPI;
    EntityTeamStore teamStore;
    MapAPI mapAPI;
    MapStore mapStore;

    public CollisionBulletDamageSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        dispatchAPI = binder.getApi(CollisionDispatchAPI.class);
        positionStore = binder.getStore(PositionStore.class);
        bulletDamageStore = binder.getStore(BulletDamageStore.class);
        bulletLifetimeAPI = binder.getApi(BulletLifetimeAPI.class);
        aoeAPI = binder.getApi(TileBucketQueryAPI.class);
        damageAPI = binder.getApi(DamageAPI.class);
        teamStore = binder.getStore(EntityTeamStore.class);
        mapAPI = binder.getApi(MapAPI.class);
        mapStore = binder.getStore(MapStore.class);
        dispatchAPI.onEe(this::onEe);
    }

    @Override
    public void run(float delta) {
    }

    public void onEe(int eidA, int eidB) {
        tryHit(eidA, eidB);
        tryHit(eidB, eidA);
    }

    public void tryHit(int bullet, int target) {
        int slot = bulletDamageStore.slotOf(bullet);
        if (slot == ArrayEntityStoreBase.NO_EID) return;

        // 队伍检查
        int teamBullet = teamStore.get(bullet);
        int teamTgt = teamStore.get(target);
        if (teamBullet == teamTgt) return;

        // 命中检查
        boolean consumed = bulletLifetimeAPI.tryConsume(bullet);

        if (consumed) {
            int posSlot = positionStore.slotOf(bullet);
            float bulletX = positionStore.x.get(posSlot);
            float bulletY = positionStore.y.get(posSlot);

            // aoe查询+造成直伤
            float radius = bulletDamageStore.splashRadiusArray.get(slot);
            float splashDmg = bulletDamageStore.splashDamageArray.get(slot);
            if (radius > 0f && splashDmg > 0f) {
                IntArray candidates = aoeAPI.collectCircle(bulletX, bulletY, radius);
                for (int i = 0; i < candidates.size; i++) {
                    int candEid = candidates.get(i);
                    int candTeam = teamStore.get(candEid);
                    if (candTeam == teamBullet) continue;

                    int candPosSlot = positionStore.slotOf(candEid);
                    float dx = positionStore.x.get(candPosSlot) - bulletX;
                    float dy = positionStore.y.get(candPosSlot) - bulletY;
                    if (dx * dx + dy * dy > radius * radius) continue;// TODO不是合格的正方形与圆相交判断
                    damageAPI.realDamage(candEid, splashDmg);
                }
            }

            // 地块染色, cs = cellSplash
            float csDmg = bulletDamageStore.cellSplashDamageArray.get(slot);
            float csRadius = bulletDamageStore.cellSplashRadiusArray.get(slot);
            float r2 = csRadius * csRadius;
            if (csDmg > 0f && csRadius > 0f) {
                int startX = MathUtils.clamp(MathUtils.round(bulletX - csRadius), 0, mapStore.width - 1);
                int startY = MathUtils.clamp(MathUtils.round(bulletY - csRadius), 0, mapStore.height - 1);
                int endX = MathUtils.clamp(MathUtils.round(bulletX + csRadius), 0, mapStore.width - 1);
                int endY = MathUtils.clamp(MathUtils.round(bulletY + csRadius), 0, mapStore.height - 1);
                int team = teamStore.get(bullet);
                for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {
                        if ((x - bulletX) * (x - bulletX) + (y - bulletY) * (y - bulletY) > r2) continue;
                        mapAPI.coverCell(csDmg, mapStore.index(x, y), team);
                    }
                }

            }

            // 直伤
            float dmg = bulletDamageStore.directDamageArray.get(slot);
            if (dmg != 0f) {
                damageAPI.realDamage(target, dmg);
            }

        }

    }
}
