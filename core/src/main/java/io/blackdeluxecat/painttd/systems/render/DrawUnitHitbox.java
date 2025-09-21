package io.blackdeluxecat.painttd.systems.render;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.systems.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;

import static io.blackdeluxecat.painttd.Core.*;

public class DrawUnitHitbox extends IteratingSystem{
    public static boolean pref = false;

    public ComponentMapper<PositionComp> pm;
    public ComponentMapper<HitboxComp> hbm;
    public ComponentMapper<HealthComp> hm;

    public DrawUnitHitbox(){
        super(Aspect.all(PositionComp.class).exclude(TileComp.class, TileStainComp.class));
    }

    @Override
    protected void initialize(){
        super.initialize();
        pref = Core.prefs.getBoolean("实体碰撞箱");
    }

    @Override
    protected void process(int entityId){
        PositionComp pos = pm.get(entityId);
        shaper.begin(ShapeRenderer.ShapeType.Line);
        Vars.c1.set(Color.WHITE, 0.2f);
        HealthComp hpc = hm.get(entityId);
        if(hpc != null && hpc.maxHealth != -1){
            Vars.c1.lerp(Color.RED, 1 - hpc.health / hpc.maxHealth).a = 1f;
        }
        shaper.getColor().set(Vars.c1);
        HitboxComp hb = hbm.get(entityId);
        boolean hasSize = hb != null;
        float w = hasSize ? hb.width : 1;
        float h = hasSize ? hb.height : 1;
        shaper.rect(pos.x - w / 2f, pos.y - h / 2f, w, h);
        shaper.end();
    }
}
