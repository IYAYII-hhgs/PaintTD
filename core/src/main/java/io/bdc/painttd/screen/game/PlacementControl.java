package io.bdc.painttd.screen.game;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.assemble.step.*;
import io.bdc.painttd.world.store.*;

/**
 * 放置指令发生器
 * 向WorldRuntime发出放置指令
 * TODO 开发调试期直接生成SpawnRequest, 后续需转接放置条件Validator
 */

public class PlacementControl {
    public WorldRuntime world;

    public @Null EntityDef select;
    public boolean placeWall, placeCore, placeCellHp;
    public Array<PostSpawnStep> extraSteps = new Array<>();

    public float placeCellHpAmt = 1;
    public int placeCellHpSize = 0;

    public PlacementControl(WorldRuntime world) {
        this.world = world;
    }

    public void setSelect(EntityDef def) {
        select = def;
        placeCore = placeWall = placeCellHp = false;
    }

    public void toggle(EntityDef def) {
        if (select == def) {
            select = null;
        } else {
            select = def;
            placeCore = placeWall = placeCellHp = false;
        }
    }

    public void toggleWall() {
        if (placeWall) {
            placeWall = false;
        } else {
            placeWall = true;
            placeCellHp = false;
            placeCore = false;
            select = null;
        }
    }

    public void toggleCore() {
        if (placeCore) {
            placeCore = false;
        } else {
            placeCore = true;
            placeCellHp = false;
            placeWall = false;
            select = null;
        }
    }

    public void toggleCellHp() {
        if (placeCellHp) {
            placeCellHp = false;
        } else {
            placeCellHp = true;
            placeCore = false;
            placeWall = false;
            select = null;
        }
    }

    public void setCellHpAmt(float amt) {
        placeCellHpAmt = amt;
    }

    public void addPostSteps(PostSpawnStep... steps) {
        extraSteps.addAll(steps);
    }

    public void clearPostSteps() {
        extraSteps.clear();
    }

    public boolean place(float inputX, float inputY) {
        if (select != null) {
            return place(select, inputX, inputY, extraSteps);
        } else if (placeWall) {
            return placeWall(inputX, inputY);
        } else if (placeCore) {
            return placeCore(inputX, inputY);
        } else if (placeCellHp) {
            return placeCellHp(inputX, inputY);
        } else {
            return false;
        }
    }

    private boolean place(EntityDef type, float inputX, float inputY, Array<PostSpawnStep> steps) {
        if (type == null) return false;
        var queue = world.getStore(SpawnRequestQueue.class);
        var req = SpawnRequestQueue.obtain();
        req.setup(type, inputX, inputY);
        req.extraSteps.addAll(steps);

        queue.add(req);

        return true;
    }

    private boolean placeWall(float inputX, float inputY) {
        var store = world.getStore(MapStore.class);

        int index = store.index(MathUtils.floor(inputX), MathUtils.floor(inputY));
        //切换墙壁
        store.cells[index] = store.cells[index] == 0 ? 1 : 0;

        return true;
    }

    private boolean placeCore(float inputX, float inputY) {
        var store = world.getStore(MapStore.class);

        int index = store.index(MathUtils.floor(inputX), MathUtils.floor(inputY));
        //切换核心
        store.coreMask[index] = !store.coreMask[index];

        return true;
    }

    private boolean placeCellHp(float inputX, float inputY) {
        var api = world.getApi(MapAPI.class);
        var store = world.getStore(MapStore.class);
        int cx = MathUtils.floor(inputX);
        int cy = MathUtils.floor(inputY);
        for (int x = cx - placeCellHpSize; x <= cx + placeCellHpSize; x++) {
            for (int y = cy - placeCellHpSize; y <= cy + placeCellHpSize; y++) {
                if (x >= 0 && x < store.width && y >= 0 && y < store.height) {
                    api.setCellHp(placeCellHpAmt, store.index(x, y));
                }
            }
        }
        return true;
    }
}
