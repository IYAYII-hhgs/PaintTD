package io.blackdeluxecat.painttd.content.components.logic;

import io.blackdeluxecat.painttd.content.components.*;

public class TileLayerComp extends CopyableComponent{
    public int layer;

    public TileLayerComp(){}

    public TileLayerComp(int layer){
        this.layer = layer;
    }

    @Override
    public TileLayerComp copy(CopyableComponent other){
        TileLayerComp tileLayerComp = (TileLayerComp) other;
        layer = tileLayerComp.layer;
        return this;
    }

    @Override
    protected void reset(){

    }
}
