package io.bdc.painttd.world.store;

import com.badlogic.gdx.math.*;

/**
 * 地图聚合数据源
 */
public class MapStore implements WorldStore {
    public int width, height;
    //cell左下角的世界坐标==网格坐标
    //临时实现: cell[i]=0可通行格, =1墙格
    public int[] cells;

    public boolean[] coreMask;
    public int[] teamMask;
    public float[] hpMask;

    public MapStore(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new int[width * height];
        coreMask = new boolean[width * height];
        teamMask = new int[width * height];
        hpMask = new float[width * height];
    }

    public int size() {
        return width * height;
    }

    public int cellX(float worldX) {
        return MathUtils.floor(worldX);
    }

    public int cellY(float worldY) {
        return MathUtils.floor(worldY);
    }

    public float cellCenterX(int x) {
        return x + 0.5f;
    }

    public float cellCenterY(int y) {
        return y + 0.5f;
    }

    public int index(int x, int y) {
        return y * width + x;
    }
}
