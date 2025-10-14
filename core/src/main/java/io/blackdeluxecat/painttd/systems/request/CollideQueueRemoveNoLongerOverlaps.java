package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

/**
 * 移除已经不再碰撞的实体.
 */
@IsLogicProcess
public class CollideQueueRemoveNoLongerOverlaps extends BaseSystem{
    protected static Rectangle r1 = new Rectangle(), r2 = new Rectangle();

    @Override
    protected void processSystem(){

    }
}
