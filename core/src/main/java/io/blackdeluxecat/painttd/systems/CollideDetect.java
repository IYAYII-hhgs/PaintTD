package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.event.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.*;

/**
 * <p>执行碰撞检查. 用四叉树查询三倍尺寸的碰撞箱, 并将发生碰撞的实体添加到碰撞事件组件中.
 * 遍历每个持有碰撞事件组件的实体, 但可以碰撞不持有碰撞事件组件的实体, 以减少碰撞事件重复处理.</p>
 * 给碰撞双方都持有碰撞事件是可行的, 例如实体A和实体B对碰撞的处理逻辑不同时.
 * 碰撞事件的有效期自碰撞检测系统后到当前帧结束前.
 * */
public class CollideDetect extends IteratingSystem{
    public ComponentMapper<PositionComp> positionCompComponentMapper;
    public ComponentMapper<HitboxComp> hitboxCompComponentMapper;
    public ComponentMapper<CollideEventComp> collideEventCompComponentMapper;

    protected IntArray result = new IntArray();
    protected static Rectangle r1 = new Rectangle(), r2 = new Rectangle();

    public CollideDetect(){
        super(Aspect.all(PositionComp.class, HitboxComp.class, CollideEventComp.class, MarkerComp.UseQuadTree.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        positionCompComponentMapper = world.getMapper(PositionComp.class);
        hitboxCompComponentMapper = world.getMapper(HitboxComp.class);
        collideEventCompComponentMapper = world.getMapper(CollideEventComp.class);
    }

    @Override
    protected void process(int entityId){
        CollideEventComp collideEventComp = collideEventCompComponentMapper.get(entityId);
        collideEventComp.clear();

        PositionComp positionComp = positionCompComponentMapper.get(entityId);
        HitboxComp hitboxComp = hitboxCompComponentMapper.get(entityId);
        float x = positionComp.x;
        float y = positionComp.y;
        float width = hitboxComp.width;
        float height = hitboxComp.height;
        Game.entities.queryRect(x - width * 3, y - height * 3, x + width * 3, y + height * 3, result, null);

        for(int i = 0; i < result.size; i++){
            int otherId = result.get(i);
            if(otherId != entityId){
                PositionComp otherPositionComp = positionCompComponentMapper.get(otherId);
                HitboxComp otherHitboxComp = hitboxCompComponentMapper.get(otherId);
                r1.set(x, y, width, height);
                r2.set(otherPositionComp.x, otherPositionComp.y, otherHitboxComp.width, otherHitboxComp.height);
                if(r1.overlaps(r2)) collideEventComp.add(otherId);
            }
        }
    }
}