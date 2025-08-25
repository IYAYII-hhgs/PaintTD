package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.event.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

public class DamageDeal extends IteratingSystem{
    public ComponentMapper<DamageDealEventComp> drm;
    public ComponentMapper<HealthComp> hcm;

    public DamageDeal(){
        super(Aspect.all(DamageDealEventComp.class, HealthComp.class).exclude(MarkerComp.Dead.class));
    }

    @Override
    protected void process(int entityId){
        DamageDealEventComp damageReceived = drm.get(entityId);
        HealthComp hp = hcm.get(entityId);

        for(int i = 0; i < damageReceived.damages.size; i++){
            hp.health -= damageReceived.damages.get(i);
        }

        damageReceived.damages.clear();
    }
}
