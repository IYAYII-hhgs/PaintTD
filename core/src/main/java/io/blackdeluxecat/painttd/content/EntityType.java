package io.blackdeluxecat.painttd.content;

import com.artemis.*;
import com.artemis.utils.*;
import com.badlogic.gdx.utils.reflect.*;
import io.blackdeluxecat.painttd.content.components.*;
import io.blackdeluxecat.painttd.game.*;

import static io.blackdeluxecat.painttd.game.Game.world;

public class EntityType{
    public String name;

    /**默认组件包。默认组件使用构造函数创建，不需要池化管理。由{@link #create()}创建的实体将拷贝一份受到池化管理的组件包。*/
    public Bag<CopyableComponent> def = new Bag<>();
    public Bag<String> groups = new Bag<>();

    /**推荐使用匿名构造函数编辑默认组件包*/
    public EntityType(String name){
        this.name = name;
    }

    public EntityType(String name, EntityType superType){
        this(name);
        copyType(superType);
    }

    /**创建新实例. 新实例的组件使用池化管理*/
    public Entity create(){
        Entity e = world.createEntity();
        for(CopyableComponent component : def){
            e.edit().create(component.getClass()).copy(component);
        }

        for(var group : groups){
            Game.groups.add(e, group);
        }
        return e;
    }

    /**拷贝基类的默认组件包到该子类*/
    public void copyType(EntityType superType){
        for(CopyableComponent component : superType.def){
            try{
                def.add(ClassReflection.newInstance(component.getClass()).copy(component));
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }

        for(var group : superType.groups){
            groups.add(group);
        }
    }
}
