package io.blackdeluxecat.painttd.systems.collide;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.event.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

/**
 * 处理单位碰撞染色块碰撞事件
 */
public class UnitHitAnyHealth extends IteratingSystem{
    public static Aspect filterHealth;
    public ComponentMapper<CollideEventComp> collideEventMapper;
    public ComponentMapper<DamageReceiveComp> damageReceiveMapper;

    public UnitHitAnyHealth(){
        super(Aspect.all(DamageReceiveComp.class, CollideComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        collideEventMapper = world.getMapper(CollideEventComp.class);
        damageReceiveMapper = world.getMapper(DamageReceiveComp.class);
        filterHealth = Aspect.all(DamageReceiveComp.class).exclude(MarkerComp.Dead.class).build(world);
    }

    @Override
    protected void process(int entityId){
        CollideEventComp collideEventComp = collideEventMapper.get(entityId);
        if(collideEventComp != null){
            for(int i = collideEventComp.collides.size; i >=0 ; i--){
                int collideId = collideEventComp.collides.get(i);
                if(filterHealth.isInterested(world.getEntity(collideId))){
                    damageReceiveMapper.get(entityId).damages.add(1);
                    damageReceiveMapper.get(collideId).damages.add(1);
                    collideEventComp.handle(i);
                }
            }
        }
    }
}
