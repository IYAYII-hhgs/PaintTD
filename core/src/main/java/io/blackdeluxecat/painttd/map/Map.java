package io.blackdeluxecat.painttd.map;

import com.artemis.*;
import com.badlogic.gdx.utils.*;

import java.util.*;

/**
 * 基于坐标的瓦片索引.
 * */
public class Map{
    public int width, height;
    public Tile[] tiles;

    /**使用composition id - entity id调取特定实体/瓦片. 请勿随意修改实体组件组合*/
    public IntMap<int[]> tileToEntity;
    public IntIntMap entityToTile;

    public ColorPalette colorPalette;

    World world;

    public void create(World world, int width, int height, ColorPalette colorPalette){
        this.world = world;
        this.width = width;
        this.height = height;
        tiles = new Tile[width * height];
        entityToTile = new IntIntMap();
        tileToEntity = new IntMap<>();

        this.colorPalette = colorPalette;

        for(int i = 0; i < tiles.length; i++){
            tiles[i] = new Tile(i % width, i / width);
        }
    }

    public Tile unsafeGet(int x, int y){
        return tiles[x + y * width];
    }

    public Tile get(int x, int y){
        if(!validPos(x, y)) return null;
        return unsafeGet(x, y);
    }

    public Tile get(int pos){
        return get(pos % width, pos / width);
    }

    public boolean validPos(int x, int y){
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int pos(int x, int y){
        return x + y * width;
    }

    public int[] getTileToEntityMap(int compositionId){
        if(!tileToEntity.containsKey(compositionId)){
            var map = tileToEntity.put(compositionId, new int[width * height]);
            Arrays.fill(map, -1);
            return map;
        }
        return tileToEntity.get(compositionId);
    }

    public void putEntity(int e, int x, int y){
        removeEntity(e);
        if(!validPos(x, y)) return;

        entityToTile.put(e, pos(x, y));
        getTileToEntityMap(world.compositionId(e))[pos(x, y)] = e;
    }

    public void removeEntity(int e){
        int old = entityToTile.remove(e, -1);
        if(old != -1){
            getTileToEntityMap(world.compositionId(e))[old] = -1;
        }
    }

    public int getEntityUnsafe(int x, int y, int compositionId){
        return getTileToEntityMap(compositionId)[pos(x, y)];
    }

    public int getEntity(int x, int y, int compositionId){
        //lazy 有效性检查
        if(!validPos(x, y)) return -1;
        int e = getEntityUnsafe(x, y, compositionId);
        if(!world.getEntityManager().isActive(e)) return -1;
        return e;
    }
}