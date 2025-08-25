package io.blackdeluxecat.painttd.content.components.event;

import io.blackdeluxecat.painttd.content.components.*;

/**
 * 高性能的实体事件, 充分利用Artemis库的架构和性能优势.
 * 事件发布与监听系统的时机, 需慎重设计.
 * 实体不能持有多个相同事件, 需设计为数组.
 * 组件映射器将占据内存. 需避免大量声明事件类, 尽可能共用事件类.
 * */
public class EventComp extends CopyableComponent{
    public EventComp(){}

    /**事件类通常不需要拷贝.*/
    @Override
    public EventComp copy(CopyableComponent other){
        return this;
    }

    @Override
    protected void reset(){}
}
