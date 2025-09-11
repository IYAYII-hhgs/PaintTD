package io.blackdeluxecat.painttd.content.components.marker;

import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.*;

@Transient
public class MarkerComp extends CopyableComponent{
    public MarkerComp(){
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        return this;
    }

    @Override
    protected void reset(){
    }

    /**该组件标记的实体视为已失效*/
    public static class Dead extends MarkerComp{
        public Dead(){
        }
    }

    /**该组件标记的实体被自动插入四叉树*/
    @Transient
    public static class UseQuadTree extends MarkerComp{
        public UseQuadTree(){
        }
    }

    /**该组件标记的实体在放置时自动对齐网格*/
    @Transient
    public static class PlaceSnapGrid extends MarkerComp{
        public PlaceSnapGrid(){
        }
    }
}
