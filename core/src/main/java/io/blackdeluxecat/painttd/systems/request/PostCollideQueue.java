package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;

import static io.blackdeluxecat.painttd.game.Game.collideQueue;

public class PostCollideQueue extends BaseSystem{
    @Override
    protected void processSystem(){
        collideQueue.clear();
    }
}
