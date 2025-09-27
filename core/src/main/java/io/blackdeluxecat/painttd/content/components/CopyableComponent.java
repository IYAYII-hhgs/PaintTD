package io.blackdeluxecat.painttd.content.components;

import com.artemis.*;

/**
 * 可拷贝可序列化的组件
 * <p>组件默认深度序列化且不需要refill, 也可继承{@link com.badlogic.gdx.utils.Json.Serializable}自定义序列化
 * <p>不参与序列化的组件, 使用{@link com.artemis.annotations.Transient}注解
 * <p>对于{@code transient}的组件及其字段, 存档版本升级时将按{@link io.blackdeluxecat.painttd.content.EntityType}的定义重建. 不会自动从实体类型拷贝组件默认值, 需重写{@link #refill(CopyableComponent)}
 */
public abstract class CopyableComponent extends PooledComponent{

    /**将同类型组件{@code other}的属性拷贝到本组件.*/
    public abstract CopyableComponent copy(CopyableComponent other);

    /**反序列化后, 初始化属性字段.*/
    public void refill(CopyableComponent def){
    }
}
