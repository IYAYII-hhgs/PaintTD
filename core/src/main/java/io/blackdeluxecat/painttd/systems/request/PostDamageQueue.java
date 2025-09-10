package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class PostDamageQueue extends BaseSystem{
    @Override
    protected void processSystem(){
        damageQueue.clear();
    }
}
