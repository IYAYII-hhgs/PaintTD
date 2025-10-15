package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.systems.*;
import io.blackdeluxecat.painttd.utils.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class EventDamageApply extends BaseSystem{
    public Object tokenCollideDamage, tokenSplashDamage, tokenDirectDamage, tokenSplashTileStainDamage, tokenTileStainDamage;

    @All(value = {HealthComp.class})
    Aspect damagableAspect;

    ComponentMapper<HealthComp> healthMapper;

    IntArray tmp = new IntArray();

    @Override
    protected void initialize(){
        super.initialize();

        tokenCollideDamage = Events.on(EventTypes.CollideDamageEvent.class, e -> {
            if(e.handled) return;
            if(healthMapper.has(e.source) && healthMapper.has(e.target)){
                var sourceHealth = healthMapper.get(e.source);
                var targetHealth = healthMapper.get(e.target);
                if(sourceHealth.health <= 0 || targetHealth.health <= 0){
                    return;
                }
                float damage = Math.min(sourceHealth.health, targetHealth.health);
                targetHealth.health -= damage;
                sourceHealth.health -= damage;
                e.handle();
            }
        });

        tokenSplashDamage = Events.on(EventTypes.SplashDamageEvent.class, e -> {
            if(e.handled) return;
            entities.eachCircle(e.x, e.y, e.radius,
                other -> !utils.isTeammate(e.source, other) && damagableAspect.isInterested(world.getEntity(other)),
                other -> {
                    Events.fire(EventTypes.DamageEvent.class, e2 -> {
                        e2.source = e.source;
                        e2.target = other;
                        e2.damage = e.damage;
                    });
                });
            e.handle();
        });

        tokenDirectDamage = Events.on(EventTypes.DamageEvent.class, e -> {
            if(e.handled) return;
            if(healthMapper.has(e.target)){
                var targetHealth = healthMapper.get(e.target);
                if(targetHealth.health <= 0)
                    return;

                targetHealth.health -= e.damage;
                e.handle();
            }
        });

        tokenSplashTileStainDamage = Events.on(EventTypes.StainSplashDamageEvent.class, e -> {
            tmp.clear();
            map.queryCircle(map.stains, Math.round(e.x), Math.round(e.y), (int)e.radius, tmp);
            for(int j = 0; j < tmp.size; j++){
                int tileStain = tmp.get(j);
                Events.fire(EventTypes.StainDamageEvent.class, e2 -> {
                    e2.source = e.source;
                    e2.target = tileStain;
                    e2.damage = e.damage;
                    e2.team = e.team;
                });
            }
        });

        tokenTileStainDamage = Events.on(EventTypes.StainDamageEvent.class, e -> {
            if(e.handled) return;
            if(e.target != -1){
                utils.putTileStain(e.target, e.team, e.damage);
            }
        });
    }

    @Override
    protected void dispose(){
        super.dispose();
        Events.off(tokenCollideDamage);
        Events.off(tokenSplashDamage);
        Events.off(tokenDirectDamage);
        Events.off(tokenSplashTileStainDamage);
        Events.off(tokenTileStainDamage);
    }

    @Override
    protected void processSystem(){
    }
}
