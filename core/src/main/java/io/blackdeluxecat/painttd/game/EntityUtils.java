package io.blackdeluxecat.painttd.game;

import com.artemis.*;
import io.blackdeluxecat.painttd.content.components.logic.*;

import static io.blackdeluxecat.painttd.game.Game.*;

/**仅用于新建实体时便捷地设置各种属性，非性能最优，避免在System中使用！*/
public class EntityUtils{
    public static void setPosition(Entity e, float x, float y){
        PositionComp p = world.getMapper(PositionComp.class).get(e);
        p.x = x;
        p.y = y;
    }

    public static void setEnergySystem(Entity e, float cap, float init, boolean hasRegen, float regenRate){
        EnergyComp ec;
        if((ec = world.getMapper(EnergyComp.class).get(e)) != null){
            ec.energy = init;
            ec.maxEnergy = cap;
        }

        EnergyRegenComp erc;
        if(hasRegen && (erc = world.getMapper(EnergyRegenComp.class).get(e)) != null){
            erc.regenRate = regenRate;
        }
    }
}
