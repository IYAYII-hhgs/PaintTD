package io.blackdeluxecat.painttd.content.components.logic;

import com.artemis.*;

public abstract class CopyableComponent extends PooledComponent{
    public abstract CopyableComponent copy(CopyableComponent other);
}
