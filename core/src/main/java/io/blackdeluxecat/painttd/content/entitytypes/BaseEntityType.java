package io.blackdeluxecat.painttd.content.entitytypes;

import com.artemis.*;
import com.artemis.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.*;

public class BaseEntityType{
    public String name;
    /**默认组件包。默认组件使用构造函数创建，不需要池化管理。由{@link #create()}创建的实体将拷贝一份受到池化管理的组件包。*/
    public Bag<Component> def = new Bag<>();

    /**使用匿名构造函数以编辑默认组件包*/
    public BaseEntityType(String name){
        this.name = name;
    }

    public Entity create(){
        Entity e = Game.world.createEntity();
        for(Component component : def){
            if(component instanceof CopyableComponent cm){
                ((CopyableComponent)e.edit().create(component.getClass())).copy(cm);
            }
        }
        return e;
    }
}
