package io.bdc.painttd.content;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.def.*;
import io.bdc.painttd.world.assemble.step.asm.*;
import io.bdc.painttd.world.family.targeting.*;
import io.bdc.painttd.world.family.turret.*;
import io.bdc.painttd.world.family.weapon.*;
import io.bdc.painttd.world.family.weapon.common.*;
import io.bdc.painttd.world.store.*;

/**
 * 全局静态实体定义表
 */

public class Entities {
    private static int lastId = -1;
    public static int nextId() {
        return ++lastId;
    }
    public static Array<EntityDef> allEntityDefs = new Array<>();
    public static void registerDef(EntityDef def) {
        allEntityDefs.add(def);
        def.id = nextId();
    }

    public static EntityDef test, testBuilding, pencil, fillBucket;

    public static void load() {
        registerDef(test = new EntityDef("test") {
            {
                steps.add(new PositionStep());
                steps.add(new HitboxStep().setup(0.8f));
                steps.add(new VelocityStep());
                steps.add(new CollisionBodyStep().setup(CollisionBodyStore.BODY_DYNAMIC));
                steps.add(new EntityHealthStep().setup(1, 1));
                steps.add(new DebugTeamStep().setup(1));
            }
        });

        registerDef(testBuilding = new EntityDef("testBuilding") {
            {
                steps.add(new BuildingPositionStep());
                steps.add(new HitboxStep().setup(1));
                steps.add(new VelocityStep());
                steps.add(new CollisionBodyStep().setup(CollisionBodyStore.BODY_STATIC));
                steps.add(new EntityHealthStep().setup(4, 4));
                steps.add(new DebugTeamStep().setup(0));
            }
        });

        registerDef(pencil = new EntityDef("Pencil") {
            {
                turretDef = new TurretDef(0.5f, 4f);
                weaponDef = new WeaponDef().setup(0.5f, new LaserAttackAct());
                steps.add(new TurretDefStep().setup(new TurretDef(60f, 8f)));
                steps.add(new WeaponStep().setup(weaponDef));
                steps.add(new EntityTargetingStep().setup(EntityTargetingStore.Targeting.DISTANCE_MIN));
                steps.add(new CellTargetingStep().setup(CellTargetingStore.CellStrategy.NEAREST_EDGE));
                steps.add(new BuildingPositionStep());
                steps.add(new HitboxStep().setup(1));
                steps.add(new CollisionBodyStep().setup(CollisionBodyStore.BODY_STATIC));
                steps.add(new EntityHealthStep().setup(100, 100));
                steps.add(new DebugTeamStep().setup(0));
            }
        });
    }
}
