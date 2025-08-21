package io.blackdeluxecat.painttd.content;

import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

public class Entities{
    public static EntityType
        //enemies
        unit, eraser, tileStain,
        //turrets
        building, pencil,
        //map
        tile;

    public static void createBuilding(){
        building = new EntityType("building"){
            {
                groups.add("building");
                def.add(new MarkerComp.PlaceSnapGrid());
                def.add(new PositionComp());
                def.add(new HitboxComp(1));
            }
        };

        pencil = new EntityType("pencil", building){
            {
                def.add(new MarkerComp.UseQuadTree());
                def.add(new TeamComp(0));

                def.add(new HealthComp(1));
                def.add(new DamageReceiveComp());

                def.add(new EnergyComp(2));
                def.add(new EnergyRegenComp(2));

                def.add(new RangeComp(8));
                def.add(new DamageComp(1));
                def.add(new TargetPriorityComp(TargetPriorityComp.CLOSEST));
                def.add(new TargetComp());
                def.add(new CooldownComp(60f));
            }
        };
    }

    public static void createUnit(){
        unit = new EntityType("unit"){
            {
                groups.add("unit");
                def.add(new PositionComp());
                def.add(new HitboxComp(1));
            }
        };

        eraser = new EntityType("eraser", unit){
            {
                def.add(new MarkerComp.UseQuadTree());
                def.add(new TeamComp(1));

                def.add(new HealthComp(8));
                def.add(new DamageReceiveComp());

                def.add(new MoveSpeedComp(1));
                def.add(new VelocityComp());
                def.add(new CollideComp(CollideComp.UNIT, false).setCollidesMask(CollideComp.ALL));

            }
        };

        tileStain = new EntityType("tileStain", unit){
            {
                def.add(new MarkerComp.UseQuadTree());
                def.add(new MarkerComp.PlaceSnapGrid());
                def.add(new HealthComp(1));
                def.add(new CollideComp(CollideComp.OVERLAY, true).setCollidesMask(CollideComp.ENTITY));
            }
        };
    }


    public static void createMap(){
        /* 地图瓦片的基本属性 */
        tile = new EntityType("tile"){
            {
                groups.add("map");
                def.add(new MarkerComp.PlaceSnapGrid());
                def.add(new HitboxComp(1));

                def.add(new CollideComp(CollideComp.FLOOR, true).setCollidesMask(CollideComp.ENTITY));
            }
        };
    }

    public static void create(){
        createMap();
        createUnit();
        createBuilding();
    }
}
