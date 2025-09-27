package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.utils.reflect.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.content.components.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.*;

@IsLogicProcess
public class SpawnerSpawn extends IteratingSystem{
    public ComponentMapper<SpawnGroupComp> spawnGroupMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<HealthComp> healthMapper;
    public ComponentMapper<SpawnGroupCompsComp> spawnGroupCompsMapper;

    public SpawnerSpawn(){
        super(Aspect.all(SpawnGroupComp.class));
    }

    @Override
    protected void process(int entityId){
        SpawnGroupComp sg = spawnGroupMapper.get(entityId);
        if(Math.floorMod(Game.rules.ticks, sg.spawnDelta) == 0 && sg.amt > 0){
            sg.amt--;
            var e = EntityTypes.eraser.create();
            var spawnerPos = positionMapper.get(entityId);
            var ePos = positionMapper.get(e);
            ePos.copy(spawnerPos);
            var health = healthMapper.get(e);
            health.health = sg.health;

            var sgc = spawnGroupCompsMapper.get(entityId);
            if(sgc != null){
                try{
                    for(int i = 0; i < sgc.comps.size; i++){
                        var comp = sgc.comps.get(i);
                        e.edit().add(ClassReflection.newInstance(comp.getClass()).copy(comp));
                    }
                }catch(ReflectionException ex){
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
