package io.blackdeluxecat.painttd.content.components.logic.physics;

import com.artemis.annotations.*;
import io.blackdeluxecat.painttd.content.components.*;

/**
 * 碰撞组件. 具有该组件的实体将参与碰撞检测, 并产生碰撞事件.
 */
@Transient
public class CollideComp extends CopyableComponent{
    public static int FLOOR = 0, OVERLAY = 1, UNIT = 2, BUILDING = 3;
    public static long MAP = 1L << FLOOR | 1L << OVERLAY, ENTITY = 1L << UNIT | 1L << BUILDING, ALL = MAP | ENTITY;

    /**
     * 可碰撞层
     */
    public long collidesMask;   //64位够你折腾了
    /**
     * 碰撞层
     */
    public int collideIndex;
    public boolean solid = false;

    public CollideComp(){
    }

    /**
     * 构造一个指定了碰撞层的单位.
     */
    public CollideComp(int collideIndex, boolean solid){
        this.collideIndex = collideIndex;
        this.solid = solid;
    }

    public CollideComp setCollidesMask(Long mask){
        this.collidesMask = mask;
        return this;
    }

    public CollideComp setCollidesWith(int index, boolean value){
        if(value){
            collidesMask |= 1L << index;
        }else{
            collidesMask &= ~(1L << index);
        }
        return this;
    }

    public boolean isCollideWith(int index){
        return (collidesMask & (1L << index)) != 0;
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        CollideComp c = (CollideComp)other;
        this.collidesMask = c.collidesMask;
        this.collideIndex = c.collideIndex;
        this.solid = c.solid;
        return this;
    }

    @Override
    protected void reset(){
        this.collidesMask = 0;
        this.collideIndex = 0;
        this.solid = false;
    }

    @Override
    public void refill(CopyableComponent def){
        copy(def);
    }
}
