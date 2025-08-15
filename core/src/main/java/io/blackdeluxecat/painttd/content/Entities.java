package io.blackdeluxecat.painttd.content;

import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.movement.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

public class Entities{
    public static EntityType
        //enemies
        unit, eraser,
        //turrets
        building, pencil;

    public static void createBuilding(){
        building = new EntityType("building"){
            {
                groups.add("building");
                def.add(new MarkerComp.PlaceSnapGrid());
                def.add(new PositionComp());
                def.add(new HealthComp(1));
                def.add(new HitboxComp(1));
            }
        };

        pencil = new EntityType("pencil", building){
            {
                def.add(new EnergyComp(2));
                def.add(new EnergyRegenComp(2));
                def.add(new TargetPriorityComp(TargetPriorityComp.CLOSEST));
                def.add(new TargetComp());
                def.add(new RangeComp(8));
                def.add(new CooldownComp(1));
                def.add(new DamageComp(1));
                def.add(new MarkerComp.UseQuadTree());
            }
        };
    }

    public static void createUnit(){
        unit = new EntityType("unit"){
            {
                groups.add("unit");
                def.add(new PositionComp());
                def.add(new HealthComp(1));
                def.add(new ArmorComp(0));
                def.add(new HitboxComp(1));
                def.add(new MoveSpeedComp(1));
                def.add(new VelocityComp());
            }
        };

        eraser = new EntityType("eraser", unit){
            {
                def.add(new MarkerComp.UseQuadTree());
            }
        };
    }


    public static void createMap(){

    }

    public static void create(){
        createMap();
        createUnit();
        createBuilding();
    }
}
