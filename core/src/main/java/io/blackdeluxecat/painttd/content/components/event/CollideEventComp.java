package io.blackdeluxecat.painttd.content.components.event;

import com.badlogic.gdx.utils.*;

/**
 * 碰撞事件组件. 任何可以碰撞的实体应持有该组件.
 * */
public class CollideEventComp extends EventComp{
    public IntArray collides = new IntArray();  //方便的实现, 相比整数数组性能略降

    public CollideEventComp(){}

    public void add(int entityId){
        collides.add(entityId);
    }

    public void handle(int entityId){
        collides.removeValue(entityId);
    }

    public void remove(int entityId){
        collides.removeValue(entityId);
    }

    public void clear(){
        collides.clear();
    }

    @Override
    protected void reset(){
        super.reset();
        collides.clear();
    }
}
