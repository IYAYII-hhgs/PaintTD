package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class TileComp extends CopyableComponent{
    public boolean isWall;
    public int layer;

    public TileComp(){
    }

    public TileComp(boolean isWall, int layer){
        this.isWall = isWall;
        this.layer = layer;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        TileComp tile = (TileComp) other;
        this.isWall = tile.isWall;
        this.layer = tile.layer;
        return this;
    }

    @Override
    protected void reset(){
    }
}
