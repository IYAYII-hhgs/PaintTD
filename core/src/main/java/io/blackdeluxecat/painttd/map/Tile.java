package io.blackdeluxecat.painttd.map;

import static io.blackdeluxecat.painttd.game.Game.flowField;

/**地图瓦片类, 维护该瓦片的基本属性.*/
public class Tile{
    public int x, y;
    public int layer = 1;
    public boolean isWall;

    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setLayer(int layer){
        this.layer = layer;
        fireChange();
    }

    public void setWall(boolean isWall){
        this.isWall = isWall;
        fireChange();
    }

    public void fireChange(){
        flowField.change(x, y);
    }
}
