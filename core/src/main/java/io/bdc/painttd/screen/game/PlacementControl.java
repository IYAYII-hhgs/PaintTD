package io.bdc.painttd.screen.game;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.request.*;
import io.bdc.painttd.world.store.*;

/**
 * 放置指令发生器
 * 向WorldRuntime发出放置指令
 * TODO 开发调试期直接生成SpawnRequest, 后续需转接放置条件Validator
 */

public class PlacementControl {
    public WorldRuntime world;

    public @Null EntityDef select;
    public boolean placeWall, placeCore;

    public PlacementControl(WorldRuntime world) {
        this.world = world;
    }

    public void toggle(EntityDef def) {
        if (select == def) {
            select = null;
        } else {
            select = def;
            placeCore = placeWall = false;
        }
    }

    public void toggleWall() {
        if (placeWall) {
            placeWall = false;
        } else {
            placeWall = true;
            placeCore = false;
            select = null;
        }
    }

    public void toggleCore() {
        if (placeCore) {
            placeCore = false;
        } else {
            placeCore = true;
            placeWall = false;
            select = null;
        }
    }

    public boolean place(float inputX, float inputY) {
        if (select != null) {
            return place(select, inputX, inputY);
        } else if (placeWall) {
            return placeWall(inputX, inputY);
        } else if (placeCore) {
            return placeCore(inputX, inputY);
        } else {
            return false;
        }
    }

    private boolean place(EntityDef type, float inputX, float inputY) {
        if (type == null) return false;
        var queue = world.getStore(SpawnRequestQueue.class);

        queue.add(new EntitySpawnRequest().setup(type, inputX, inputY));

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
}
