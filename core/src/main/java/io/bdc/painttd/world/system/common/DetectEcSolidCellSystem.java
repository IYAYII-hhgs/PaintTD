package io.bdc.painttd.world.system.common;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

/**
 * EC 检测层的首版实现。
 * 负责把实体与墙格的接触转成 (eid, cellIndex) 并直接发给分发层
 * <p>
 * 只产出实体与墙格接触事实, 不处理应用效果。
 */
public class DetectEcSolidCellSystem extends WorldSystem {
    public CollisionBodyStore collisionBodyStore;
    public TransformStore transformStore;
    public HitboxStore hitboxStore;
    public MapStore mapStore;
    public CollisionDispatchAPI collisionDispatchAPI;
    public CollisionDebugStore collisionDebugStore;

    public DetectEcSolidCellSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        collisionBodyStore = binder.getStore(CollisionBodyStore.class);
        transformStore = binder.getStore(TransformStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
        mapStore = binder.getStore(MapStore.class);
        collisionDispatchAPI = binder.getApi(CollisionDispatchAPI.class);
        collisionDebugStore = binder.getStore(CollisionDebugStore.class);
    }

    @Override
    public void run(float delta) {
        for (int slot = 0; slot < collisionBodyStore.size(); slot++) {
            int eid = collisionBodyStore.eidOf(slot);
            if (!collisionBodyStore.isDynamic(eid)) {
                continue;
            }
            if (!transformStore.has(eid) || !hitboxStore.has(eid)) {
                continue;
            }

            float size = hitboxStore.get(eid);
            if (size <= 0f) {
                continue;
            }

            float half = size * 0.5f;
            float x = transformStore.getX(eid);
            float y = transformStore.getY(eid);
            int minCellX = MathUtils.floor(x - half);
            int maxCellX = MathUtils.floor(x + half - MathUtils.FLOAT_ROUNDING_ERROR);
            int minCellY = MathUtils.floor(y - half);
            int maxCellY = MathUtils.floor(y + half - MathUtils.FLOAT_ROUNDING_ERROR);
            if (maxCellX < 0 || maxCellY < 0 || minCellX >= mapStore.width || minCellY >= mapStore.height) {
                continue;
            }

            minCellX = MathUtils.clamp(minCellX, 0, mapStore.width - 1);
            maxCellX = MathUtils.clamp(maxCellX, 0, mapStore.width - 1);
            minCellY = MathUtils.clamp(minCellY, 0, mapStore.height - 1);
            maxCellY = MathUtils.clamp(maxCellY, 0, mapStore.height - 1);
            for (int cellY = minCellY; cellY <= maxCellY; cellY++) {
                for (int cellX = minCellX; cellX <= maxCellX; cellX++) {
                    int cellIndex = mapStore.index(cellX, cellY);
                    if (mapStore.cells[cellIndex] != 1) {
                        continue;
                    }

                    collisionDebugStore.ecWallCount += 1;
                    collisionDispatchAPI.emitEc(eid, cellIndex);
                }
            }
        }
    }
}
