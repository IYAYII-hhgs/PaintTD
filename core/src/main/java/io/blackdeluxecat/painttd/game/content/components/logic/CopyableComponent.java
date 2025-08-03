package io.blackdeluxecat.painttd.game.content.components.logic;

import com.artemis.*;

public abstract class CopyableComponent extends PooledComponent{
    public abstract CopyableComponent copy(CopyableComponent other);
}
