package io.blackdeluxecat.painttd.game.content.components;

import com.artemis.*;

public abstract class CloneableComponent extends PooledComponent{
    public abstract CloneableComponent copy();
}
