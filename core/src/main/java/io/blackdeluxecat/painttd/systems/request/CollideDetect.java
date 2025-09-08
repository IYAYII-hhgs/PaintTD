package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.collideQueue;
import static io.blackdeluxecat.painttd.game.Game.entities;

/**
 * 执行碰撞检查. 用四叉树查询三倍尺寸的碰撞箱, 并产生碰撞请求.
 * 遍历每个使用四叉树的实体.
 * */
@IsLogicProcess
public class CollideDetect extends IteratingSystem{
    protected IntArray result = new IntArray();
    protected static Rectangle r1 = new Rectangle(), r2 = new Rectangle();

    public CollideDetect(){
        super(Aspect.all(PositionComp.class, HitboxComp.class, MarkerComp.UseQuadTree.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
    }

    @Override
    protected void process(int entityId){
        entities.hitbox(entityId, r1);
        result.clear();
        entities.queryRect(r1.x - r1.width, r1.y - r1.height, r1.width * 3, r1.height * 3, result, null);

        for(int i = 0; i < result.size; i++){
            int otherId = result.get(i);
            if(otherId != entityId){
                entities.hitbox(entityId, r1);
                entities.hitbox(otherId, r2);
                if(r1.overlaps(r2) && !collideQueue.isColliding(entityId, otherId)){
                    collideQueue.add(entityId, otherId);
                }
            }
        }
    }
}