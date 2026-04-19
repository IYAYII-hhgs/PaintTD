package io.bdc.painttd.world.system.render;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.infra.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class DrawTileBucketDebugSystem extends WorldSystem {
    public TileBucketStore bucketStore;
    public TileBucketDebugStore debugStore;
    public TileBucketQueryAPI queryApi;
    public PositionStore positionStore;
    public HitboxStore hitboxStore;

    private final Vector2 mouseWorld = new Vector2();
    private final Vector2 entityPos = new Vector2();
    private final Rectangle rect = new Rectangle();

    public DrawTileBucketDebugSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        bucketStore = binder.getStore(TileBucketStore.class);
        debugStore = binder.getStore(TileBucketDebugStore.class);
        queryApi = binder.getApi(TileBucketQueryAPI.class);
        positionStore = binder.getStore(PositionStore.class);
        hitboxStore = binder.getStore(HitboxStore.class);
    }

    @Override
    public void run(float delta) {
        if (!debugStore.showBucketOverlay
                && !debugStore.showSelectedCoverage
                && !debugStore.showHoverCellQuery) {
            return;
        }

        if (debugStore.showBucketOverlay) {
            drawBucketOverlay();
        }
        if (debugStore.showSelectedCoverage && world.worldView.canHandleWorldInput()) {
            drawHoverEntityCoverage();
        }
        if (debugStore.showHoverCellQuery && world.worldView.canHandleWorldInput()) {
            drawHoverCellQuery();
        }
    }

    private void drawBucketOverlay() {
        float scl = RenderHub.scl;
        for (int cell = 0; cell < bucketStore.cellCount(); cell++) {
            int load = 0;
            for (int node = bucketStore.headByCell[cell]; node != -1; node = bucketStore.nextInCell[node]) {
                load += 1;
            }
            if (load == 0) {
                continue;
            }

            int x = cell % bucketStore.width;
            int y = cell / bucketStore.width;
            float alpha = Math.min(0.08f + load * 0.04f, 0.28f);
            RenderHub.batch.setColor(0.25f, 0.8f, 1f, alpha);
            RenderHub.fill.rect(x * scl, y * scl, scl, scl);
        }
    }

    private void drawHoverEntityCoverage() {
        screenToTileWorld(mouseWorld);
        int cellX = MathUtils.floor(mouseWorld.x);
        int cellY = MathUtils.floor(mouseWorld.y);
        if (!bucketStore.inBounds(cellX, cellY)) {
            return;
        }

        IntArray candidates = queryApi.collectCell(cellX, cellY);
        int eid = findNearestEntity(candidates, mouseWorld.x, mouseWorld.y);
        if (eid < 0) {
            return;
        }

        float scl = RenderHub.scl;
        RenderHub.batch.setColor(Color.GOLD);
        RenderHub.line.setStroke(1.5f);
        for (int node = bucketStore.firstNodeOfEntity(eid); node != -1; node = bucketStore.nextOfEntity[node]) {
            int cell = bucketStore.cellByNode[node];
            int x = cell % bucketStore.width;
            int y = cell / bucketStore.width;
            RenderHub.line.rect(x * scl, y * scl, scl, scl);
        }
    }

    private void drawHoverCellQuery() {
        screenToTileWorld(mouseWorld);
        int cellX = MathUtils.floor(mouseWorld.x);
        int cellY = MathUtils.floor(mouseWorld.y);
        if (!bucketStore.inBounds(cellX, cellY)) {
            return;
        }

        float scl = RenderHub.scl;
        RenderHub.batch.setColor(Color.CYAN);
        RenderHub.line.setStroke(1.5f);
        RenderHub.line.rect(cellX * scl, cellY * scl, scl, scl);

        IntArray results = queryApi.collectCell(cellX, cellY);
        for (int i = 0; i < results.size; i++) {
            int eid = results.get(i);
            if (!positionStore.has(eid)) {
                continue;
            }

            int sizeSlot = hitboxStore.slotOf(eid);
            float size = sizeSlot == -1 ? 1f : hitboxStore.hb.items[sizeSlot];

            int posSlot = positionStore.slotOf(eid);
            float x = positionStore.x.items[posSlot] * scl;
            float y = positionStore.y.items[posSlot] * scl;
            rect.setSize(size * scl).setCenter(x, y);

            RenderHub.batch.setColor(0.45f, 1f, 0.45f, 0.18f);
            RenderHub.fill.rect(rect.x, rect.y, rect.width, rect.height);
            RenderHub.batch.setColor(0.45f, 1f, 0.45f, 1f);
            RenderHub.line.setStroke(1.2f);
            RenderHub.line.rect(rect);
        }
    }

    private void screenToTileWorld(Vector2 out) {
        world.worldView.screenToWorld(Gdx.input.getX(), Gdx.input.getY(), out);
        out.scl(1f / RenderHub.scl);
    }

    private int findNearestEntity(IntArray candidates, float worldX, float worldY) {
        int bestEid = -1;
        float bestDst2 = Float.MAX_VALUE;
        for (int i = 0; i < candidates.size; i++) {
            int eid = candidates.get(i);
            if (positionStore.get(eid, entityPos) == null) {
                continue;
            }

            float dx = entityPos.x - worldX;
            float dy = entityPos.y - worldY;
            float dst2 = dx * dx + dy * dy;
            if (dst2 < bestDst2) {
                bestDst2 = dst2;
                bestEid = eid;
            }
        }
        return bestEid;
    }
}
