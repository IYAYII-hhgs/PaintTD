package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class ShootSingleSlashStain extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<StainSlashComp> stainSlashMapper;
    public ComponentMapper<ColorLevelComp> colorLevelMapper;
    public ComponentMapper<HealthComp> healthMapper;
    public ComponentMapper<TeamComp> teamMapper;

    IntArray tmp = new IntArray();

    public ShootSingleSlashStain(){
        super(Aspect.all(CooldownComp.class, TargetSingleComp.class, StainSlashComp.class, ColorLevelComp.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                PositionComp tgtPos = positionMapper.get(targetSingle.targetId);
                StainSlashComp slash = stainSlashMapper.get(entityId);

                int x = tgtPos.tileX(), y = tgtPos.tileY();

                tmp.clear();
                map.queryCircle(map.stains, x, y, slash.range, tmp);
                for(int i = 0; i < tmp.size; i++){
                    int tileStain = tmp.get(i);
                    utils.putTileStain(tileStain, teamMapper.get(entityId).team, slash.level);
                }
            }
        }

    }
}
