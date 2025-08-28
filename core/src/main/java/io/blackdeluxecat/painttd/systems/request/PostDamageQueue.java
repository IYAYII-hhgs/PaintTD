package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;

import static io.blackdeluxecat.painttd.game.Game.damageQueue;

public class PostDamageQueue extends BaseSystem{
    @Override
    protected void processSystem(){
        damageQueue.clear();
    }
}
