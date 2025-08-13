package io.blackdeluxecat.painttd.map;

public class Map{
    public int width, height;
    public float[] tiles;

    public void create(int width, int height){
        this.width = width;
        this.height = height;
        tiles = new float[width * height];
    }

    public void setTile(int x, int y, float tile){
        tiles[x + y * width] = tile;
    }

    public float getTile(int x, int y){
        return tiles[x + y * width];
    }
}
