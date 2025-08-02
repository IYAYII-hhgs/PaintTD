package io.blackdeluxecat.painttd.game.content.entitytypes;

import com.artemis.*;
import com.artemis.utils.*;
import io.blackdeluxecat.painttd.game.*;

public class BaseEntityType{
    public String name;
    public Bag<Component> def = new Bag<>();

    /**使用匿名构造函数以编辑默认组件包*/
    public BaseEntityType(String name){
        this.name = name;
    }

    public Entity create(){
        Entity e = Game.world.createEntity();
        for(Component component : def){
            e.edit().create(component.getClass());
        }
        return e;
    }
}
