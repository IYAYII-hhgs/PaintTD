package io.blackdeluxecat.painttd.content.components.marker;

import io.blackdeluxecat.painttd.content.components.*;

public class MarkerComp extends CopyableComponent{

    public MarkerComp(){}

    @Override
    public CopyableComponent copy(CopyableComponent other){
        return this;
    }

    @Override
    protected void reset(){}

    public static class MapAttribute extends MarkerComp{
        public MapAttribute(){}
    }

    public static class Unit extends MarkerComp{
        public Unit(){}
    }

    public static class Building extends MarkerComp{
        public Building(){}
    }

    public static class Bullet extends MarkerComp{
        public Bullet(){}
    }

    public static class Dead extends MarkerComp{
        public Dead(){}
    }

    public static class Tile extends MarkerComp{
        public Tile(){}
    }

    public static class UseQuadTree extends MarkerComp{
        public UseQuadTree(){}
    }

    public static class PlaceSnapGrid extends MarkerComp{
        public PlaceSnapGrid(){}
    }
}
