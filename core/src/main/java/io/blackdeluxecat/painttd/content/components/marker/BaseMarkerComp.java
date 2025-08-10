package io.blackdeluxecat.painttd.content.components.marker;

import io.blackdeluxecat.painttd.content.components.*;

public class BaseMarkerComp extends CopyableComponent{

    public BaseMarkerComp(){}

    @Override
    public CopyableComponent copy(CopyableComponent other){
        return null;
    }

    @Override
    protected void reset(){}


    public static class Bullet extends BaseMarkerComp{
        public Bullet(){}
    }

    public static class Dead extends BaseMarkerComp{
        public Dead(){}
    }

    public static class Tile extends BaseMarkerComp{
        public Tile(){}
    }
}
