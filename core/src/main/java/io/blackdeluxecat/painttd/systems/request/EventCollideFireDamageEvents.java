package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.annotations.*;
import com.badlogic.gdx.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.systems.*;
import io.blackdeluxecat.painttd.utils.Events;

import static io.blackdeluxecat.painttd.game.Game.utils;

@IsLogicProcess
public class EventCollideFireDamageEvents extends BaseSystem{
    public Object tokenCollideDamageEvent, tokenCollideSplashEvent, tokenCollideStainSplashEvent, tokenOnCollideDeadEvent;

    @All(value = {TeamComp.class, HealthComp.class})
    @Exclude(MarkerComp.Dead.class)
    Aspect collideAspect;

    @All(value = {MarkerComp.OnCollideDead.class})
    @Exclude(MarkerComp.Dead.class)
    Aspect onCollideDeadAspect;

    @All(value = {DamageSplashComp.class, MarkerComp.CollideAttacker.class})
    Aspect splashAspect;

    @All(value = {StainSplashComp.class, MarkerComp.CollideAttacker.class})
    Aspect stainAspect;

    ComponentMapper<PositionComp> positionMapper;
    ComponentMapper<DamageSplashComp> damageSplashMapper;
    ComponentMapper<StainSplashComp> stainSplashMapper;

    @Override
    protected void initialize(){
        super.initialize();

        tokenCollideDamageEvent = Events.on(EventTypes.CollideEvent.class, e -> {
            if(e.handled) return;
            if(!collideAspect.isInterested(world.getEntity(e.source)) || !collideAspect.isInterested(world.getEntity(e.target))) return;
            if(utils.isTeammateOrFriendly(e.source, e.target)) return;

            Events.fire(EventTypes.CollideDamageEvent.class, e2 -> {
                e2.source = e.source;
                e2.target = e.target;
            });
        });

        tokenOnCollideDeadEvent = Events.on(EventTypes.CollideEvent.class, e -> {
            if(!onCollideDeadAspect.isInterested(world.getEntity(e.source))) return;
            utils.markDead(e.source);
        });

        tokenCollideSplashEvent = Events.on(EventTypes.CollideEvent.class, e -> {
            if(e.handled) return;
            if(utils.isTeammateOrFriendly(e.source, e.target)) return;
            if(e.source == -1 || !splashAspect.isInterested(world.getEntity(e.source))) return;

            PositionComp pos = positionMapper.get(e.source);
            DamageSplashComp dmg = damageSplashMapper.get(e.source);
            if(dmg == null) return;

            Events.fire(EventTypes.SplashDamageEvent.class, e2 -> {
                e2.source = e.source;
                e2.x = pos.x;
                e2.y = pos.y;
                e2.damage = dmg.damage;
                e2.radius = dmg.radius;
            });
        });

        tokenCollideStainSplashEvent = Events.on(EventTypes.CollideEvent.class, e -> {
            if(e.handled) return;
            if(e.source == -1 || !stainAspect.isInterested(world.getEntity(e.source))) return;

            PositionComp pos = positionMapper.get(e.source);
            StainSplashComp dmg = stainSplashMapper.get(e.source);

            Events.fire(EventTypes.StainSplashDamageEvent.class, e2 -> {
                e2.source = e.source;
                e2.x = pos.x;
                e2.y = pos.y;
                e2.damage = dmg.damage;
                e2.radius = dmg.radius;
            });
        });
    }

    @Override
    protected void dispose(){
        super.dispose();
        Events.off(tokenCollideDamageEvent);
        Events.off(tokenCollideSplashEvent);
        Events.off(tokenCollideStainSplashEvent);
    }

    @Override
    protected void processSystem(){
    }
}
