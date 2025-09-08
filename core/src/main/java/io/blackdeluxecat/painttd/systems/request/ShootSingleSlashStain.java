package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.math.*;
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

    public ShootSingleSlashStain(){
        super(Aspect.all(CooldownComp.class, TargetSingleComp.class, StainSlashComp.class, ColorLevelComp.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        cooldownMapper = world.getMapper(CooldownComp.class);
        targetSingleMapper = world.getMapper(TargetSingleComp.class);
        positionMapper = world.getMapper(PositionComp.class);
        stainSlashMapper = world.getMapper(StainSlashComp.class);
        colorLevelMapper = world.getMapper(ColorLevelComp.class);
        healthMapper = world.getMapper(HealthComp.class);
        teamMapper = world.getMapper(TeamComp.class);
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                PositionComp tgtPos = positionMapper.get(targetSingle.targetId);
                StainSlashComp slash = stainSlashMapper.get(entityId);
                ColorLevelComp colorLevel = colorLevelMapper.get(entityId);

                int x = tgtPos.tileX(), y = tgtPos.tileY();
                for(int dx = -slash.range; dx <= slash.range; dx++){
                    for(int dy = -slash.range; dy <= slash.range; dy++){
                        int tileStain = map.getTileStain(MathUtils.clamp(dx + x, 0, map.width - 1), MathUtils.clamp(dy + y, 0, map.height - 1));
                        if(tileStain != -1){
                            HealthComp health = healthMapper.get(tileStain);
                            TeamComp stainTeam = teamMapper.get(tileStain);
                            TeamComp entityTeam = teamMapper.get(entityId);
                            float level = colorLevel.level;
                            if(stainTeam.team != entityTeam.team){
                                health.health -= level;
                                if(health.health <= 0){
                                    stainTeam.team = entityTeam.team;
                                    health.health = -health.health;
                                }
                            }else{
                                health.health = Math.max(health.health, level);
                            }
                        }
                    }
                }
            }
        }

    }
}
