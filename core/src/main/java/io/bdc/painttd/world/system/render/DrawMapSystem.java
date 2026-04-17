package io.bdc.painttd.world.system.render;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import io.bdc.painttd.infra.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class DrawMapSystem extends WorldSystem {
    public MapStore map;

    private static Rectangle rect = new Rectangle();
    private static Vector2 pos = new Vector2();
    private static Color col = new Color();

    public DrawMapSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        map = binder.getStore(MapStore.class);
    }

    @Override
    public void run(float delta) {
        float scl = RenderHub.scl;
        //图格
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                int pos = map.index(x, y);

                if (map.hpMask[pos] > 0) {
                    col.set(Color.GREEN).a = MathUtils.clamp(map.hpMask[pos] / 10f, 0.2f, 1);
                    RenderHub.batch.setColor(col);
                    RenderHub.fill.rect(x * scl, y * scl, scl, scl);
                }

                RenderHub.batch.setColor(Color.DARK_GRAY);
                RenderHub.line.setStroke(0.6f);
                RenderHub.line.rect(x * scl, y * scl, scl, scl);

                if (map.cells[pos] == 1) {
                    RenderHub.batch.setColor(Color.SALMON);
                    RenderHub.fill.crect(map.cellCenterX(x) * scl, map.cellCenterY(y) * scl, scl / 2f, scl / 2f);
                }

                if (map.coreMask[pos]) {
                    RenderHub.batch.setColor(Color.WHITE);
                    RenderHub.line.setStroke(0.8f);
                    RenderHub.line.circle(map.cellCenterX(x) * scl, map.cellCenterY(y) * scl, scl / 3f, 12);
                }
            }
        }
        //地图边界
        RenderHub.batch.setColor(Color.LIGHT_GRAY);
        RenderHub.line.setStroke(1f);
        RenderHub.line.rect(0, 0, map.width * scl, map.height * scl);
    }
}
