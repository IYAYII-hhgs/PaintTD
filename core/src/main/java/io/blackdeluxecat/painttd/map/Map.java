package io.blackdeluxecat.painttd.map;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.*;

/**
 * 运行时地图类, 持有瓦片实体的id, 基于坐标的瓦片索引.
 * 读写瓦片附加属性需{@link #get(int, int)}获取瓦片实体
 * */
public class Map{
    public int width, height;
    public Tile[] tiles;

    World world;

    public void create(World world, int width, int height){
        this.world = world;
        this.width = width;
        this.height = height;
        tiles = new Tile[width * height];

        for(int i = 0; i < tiles.length; i++){
            tiles[i] = new Tile(i % width, i / width);
        }
    }

    public Tile unsafeGet(int x, int y){
        return tiles[x + y * width];
    }

    public Tile get(int x, int y){
        if(x < 0 || x >= width || y < 0 || y >= height) return null;
        return get(x, y);
    }
}