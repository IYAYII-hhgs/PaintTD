package io.blackdeluxecat.painttd.content;

import com.artemis.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.content.components.render.*;

import static io.blackdeluxecat.painttd.game.Game.*;

public class Entities{
    public static Array<EntityType> types = new Array<>();

    public static EntityType
        //enemies
        unit, eraser, tileStain,
    //turrets
    building, pencil, brush;

    public static void createBuilding(){
        building = new EntityType("building"){
            {
                addGroup("building");
                add(new MarkerComp.PlaceSnapGrid());
                add(new PositionComp());
                add(new HitboxComp(1));
            }
        };

        pencil = new EntityType("pencil", building){
            {
                add(new MarkerComp.UseQuadTree());
                add(new TeamComp(0));

                add(new HealthComp(1));

                add(new RangeComp(6));
                add(new DamageComp(1));

                add(new TargetPriorityComp(TargetPriorityComp.CLOSEST));
                add(new TargetSingleComp());
                add(new CooldownComp(60f));
                add(new ColorLevelComp(2));

                add(new PartTextureComp("b-pencil"));
            }
        };

        brush = new EntityType("brush", building){
            {
                add(new MarkerComp.UseQuadTree());
                add(new TeamComp(0));

                add(new HealthComp(1));

                add(new RangeComp(10));
                add(new DamageSlashComp(1, 1));
                add(new StainSlashComp(1));
                add(new TargetPriorityComp(TargetPriorityComp.CLOSEST));
                add(new TargetSingleComp());
                add(new CooldownComp(180f));
                add(new ColorLevelComp(1));

                add(new PartTextureComp("b-brush"));
            }
        };
    }

    public static void createUnit(){
        unit = new EntityType("unit"){
            {
                addGroup("unit");
                add(new PositionComp());
            }
        };

        eraser = new EntityType("eraser", unit){
            {
                add(new MarkerComp.UseQuadTree());
                add(new CollideComp(CollideComp.UNIT, false).setCollidesMask(CollideComp.ALL));
                add(new TeamComp(1));

                add(new HealthComp(16));
                add(new HitboxComp(0.8f));

                add(new MoveSpeedComp(1f / lfps));
                add(new VelocityComp());
                add(new MovementNextPathComp());

                add(new PartTextureComp("u-eraser"));
            }
        };

        tileStain = new EntityType("tileStain", unit){
            {
                addGroup("tileStain");
                add(new TileStainComp());
                add(new HitboxComp(1));
                add(new CollideComp(CollideComp.OVERLAY, true).setCollidesMask(CollideComp.ENTITY));

                add(new TeamComp(0));
                add(new HealthComp(-1));
            }
        };
    }

    public static void create(World world){
        types.clear();
        createUnit();
        createBuilding();
    }
}
