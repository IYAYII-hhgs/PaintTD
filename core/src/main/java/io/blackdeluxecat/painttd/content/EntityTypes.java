package io.blackdeluxecat.painttd.content;

import com.artemis.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.content.components.render.*;

import static io.blackdeluxecat.painttd.game.Game.*;

public class EntityTypes{
    public static Array<EntityType> types = new Array<>();
    public static ObjectMap<String, Array<EntityType>> typeByCategory = new ObjectMap<>();
    static Array<EntityType> tmp = new Array<>();

    public static void register(EntityType type){
        types.add(type);

        var arr = typeByCategory.get(type.category);
        if(arr == null) typeByCategory.put(type.category, arr = new Array<>());
        arr.add(type);
    }

    public static EntityType getById(String id){
        for(EntityType type : types){
            if(type.id.equals(id)){
                return type;
            }
        }
        return null;
    }

    public static Array<EntityType> getByCategory(String category){
        tmp.clear();
        tmp.addAll(typeByCategory.get(category, tmp));
        return tmp;
    }

    public static EntityType
        //enemies
        eraser,

        //turrets
        building, pencil, brush,

        //tiles
        tile, tileStain, spawner;

    public static void create(World world){
        types.clear();

        eraser = new EntityType("eraser", cUnit){
            {
                add(new MarkerComp.UseQuadTree());
                add(new MarkerComp.Hoverable());
                add(new CollideComp(CollideComp.UNIT, false).setCollidesMask(CollideComp.ALL));
                add(new TeamComp(1));

                add(new HealthComp(16));
                add(new PositionComp());
                add(new HitboxComp(0.6f));

                add(new MoveSpeedComp(1f / lfps));
                add(new VelocityComp());
                add(new MovementNextPathComp());

                add(new PartTextureComp("u-eraser"));
            }
        };

        building = new EntityType("building", cHide){
            {
                addGroup("building");
                add(new MarkerComp.PlaceSnapGrid());
                add(new MarkerComp.Hoverable());
                add(new PositionComp());
                add(new HitboxComp(1));
            }
        };

        pencil = new EntityType("pencil", building, cBuilding){
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

        brush = new EntityType("brush", building, cBuilding){
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

        tile = new EntityType("tile", cHide){
            {
                addGroup("tile");
                add(new PositionComp());
                add(new HitboxComp(1));
                add(new CollideComp(CollideComp.FLOOR, true).setCollidesMask(CollideComp.ENTITY));
                add(new TileComp());
            }
        };

        tileStain = new EntityType("tileStain", cHide){
            {
                addGroup("tileStain");
                add(new TileStainComp());
                add(new PositionComp());
                add(new HitboxComp(1));
                add(new CollideComp(CollideComp.OVERLAY, true).setCollidesMask(CollideComp.ENTITY));

                add(new TeamComp(0));
                add(new HealthComp(-1));
            }
        };

        spawner = new EntityType("spawner", building, cEditor){
            {
                addGroup("spawner");
            }
        };
    }

    public static String cBuilding = "building", cUnit = "unit", cHide = "hide", cEditor = "editor";
}
