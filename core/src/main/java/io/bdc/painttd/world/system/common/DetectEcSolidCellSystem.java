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
        int[] bodyTypeItems = collisionBodyStore.bodyTypes.items;
        float[] transformXItems = transformStore.x.items;
        float[] transformYItems = transformStore.y.items;
        float[] hitboxItems = hitboxStore.hb.items;
        int[] mapCells = mapStore.cells;
        int mapWidth = mapStore.width;
        int mapHeight = mapStore.height;

        int ecWallCount = collisionDebugStore.ecWallCount;

        for (int bodySlot = 0; bodySlot < collisionBodyStore.size(); bodySlot++) {
            if (bodyTypeItems[bodySlot] != CollisionBodyStore.BODY_DYNAMIC) {
                continue;
            }

            int eid = collisionBodyStore.eidOf(bodySlot);
            int transformSlot = transformStore.slotOf(eid);
            int hitboxSlot = hitboxStore.slotOf(eid);
            if (transformSlot < 0 || hitboxSlot < 0) {
                continue;
            }

            float size = hitboxItems[hitboxSlot];
            if (size <= 0f) {
                continue;
            }

            float half = size * 0.5f;
            float x = transformXItems[transformSlot];
            float y = transformYItems[transformSlot];
            int minCellX = MathUtils.floor(x - half);
            int maxCellX = MathUtils.floor(x + half - MathUtils.FLOAT_ROUNDING_ERROR);
            int minCellY = MathUtils.floor(y - half);
            int maxCellY = MathUtils.floor(y + half - MathUtils.FLOAT_ROUNDING_ERROR);
            if (maxCellX < 0 || maxCellY < 0 || minCellX >= mapWidth || minCellY >= mapHeight) {
                continue;
            }

            minCellX = MathUtils.clamp(minCellX, 0, mapWidth - 1);
            maxCellX = MathUtils.clamp(maxCellX, 0, mapWidth - 1);
            minCellY = MathUtils.clamp(minCellY, 0, mapHeight - 1);
            maxCellY = MathUtils.clamp(maxCellY, 0, mapHeight - 1);
            for (int cellY = minCellY; cellY <= maxCellY; cellY++) {
                int rowIndex = cellY * mapWidth;
                for (int cellX = minCellX; cellX <= maxCellX; cellX++) {
                    int cellIndex = rowIndex + cellX;
                    if (mapCells[cellIndex] != 1) {
                        continue;
                    }

                    ecWallCount += 1;
                    collisionDispatchAPI.emitEc(eid, cellIndex);
                }
            }
        }

        collisionDebugStore.ecWallCount = ecWallCount;
    }
}
