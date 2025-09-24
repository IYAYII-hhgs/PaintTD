package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.annotations.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.*;

@Wire
@IsLogicProcess
public class SpawnerSpawn extends IteratingSystem{
    public ComponentMapper<SpawnGroupComp> spawnGroupMapper;
    public ComponentMapper<PositionComp> positionMapper;

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
        }
    }
}
