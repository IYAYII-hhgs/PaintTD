package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.game.request.*;

import static io.blackdeluxecat.painttd.game.Game.collideQueue;
import static io.blackdeluxecat.painttd.game.Game.entities;

/**
 * 移除已经不再碰撞的实体.
 * */
public class CollideQueueRemoveNoLongerOverlaps extends BaseSystem{
    protected static Rectangle r1 = new Rectangle(), r2 = new Rectangle();

    @Override
    protected void processSystem(){
        for(int i = 0; i < collideQueue.queue.size; i++){
            CollideQueue.CollideRequest req = collideQueue.queue.removeFirst();
            if(world.getEntity(req.e1).isActive() && world.getEntity(req.e2).isActive() && entities.hitbox(req.e1, r1).overlaps(entities.hitbox(req.e2, r2))){
                collideQueue.queue.addLast(req);
            }else{
                collideQueue.free(req);
            }
        }
    }
}
