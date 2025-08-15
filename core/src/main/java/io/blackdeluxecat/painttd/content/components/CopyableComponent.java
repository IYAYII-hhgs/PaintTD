package io.blackdeluxecat.painttd.content.components;

import com.artemis.*;

public abstract class CopyableComponent extends PooledComponent{

    /**
     * 将同类型组件{@code other}的属性拷贝到本组件.
     */
    public abstract CopyableComponent copy(CopyableComponent other);
}
