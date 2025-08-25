package io.blackdeluxecat.painttd.systems.collide;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.event.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

import static io.blackdeluxecat.painttd.game.Game.utils;

public class UnitHitEnemyHealth extends IteratingSystem{
    public static Aspect filterHealth;
    public ComponentMapper<CollideEventComp> collideEventMapper;
    public ComponentMapper<DamageDealEventComp> damageReceiveMapper;

    public UnitHitEnemyHealth(){
        super(Aspect.all(DamageDealEventComp.class, CollideEventComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        collideEventMapper = world.getMapper(CollideEventComp.class);
        damageReceiveMapper = world.getMapper(DamageDealEventComp.class);
        filterHealth = Aspect.all(DamageDealEventComp.class).exclude(MarkerComp.Dead.class).build(world);
    }

    @Override
    protected void process(int entityId){
        CollideEventComp collideEventComp = collideEventMapper.get(entityId);
        if(collideEventComp != null){
            for(int i = collideEventComp.collides.size - 1; i >= 0; i--){
                int collideId = collideEventComp.collides.get(i);

                if(!utils.isTeammate(entityId, collideId) && filterHealth.isInterested(world.getEntity(collideId))){
                    damageReceiveMapper.get(entityId).damages.add(1);
                    damageReceiveMapper.get(collideId).damages.add(1);
                    collideEventComp.handle(i);
                }
            }
        }
    }
}
