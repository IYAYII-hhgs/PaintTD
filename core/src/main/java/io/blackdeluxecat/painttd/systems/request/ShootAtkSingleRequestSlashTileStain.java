package io.blackdeluxecat.painttd.systems.request;

import com.artemis.*;
import com.artemis.systems.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.target.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.request.*;
import io.blackdeluxecat.painttd.systems.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class ShootAtkSingleRequestSlashTileStain extends IteratingSystem{
    public ComponentMapper<CooldownComp> cooldownMapper;
    public ComponentMapper<TargetSingleComp> targetSingleMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<StainSlashComp> stainSlashMapper;

    public ShootAtkSingleRequestSlashTileStain(){
        super(Aspect.all(CooldownComp.class, TargetSingleComp.class, StainSlashComp.class, MarkerComp.ShootAttacker.class));
    }

    @Override
    protected void process(int entityId){
        CooldownComp cooldown = cooldownMapper.get(entityId);
        if(cooldown.shootCount > 0){
            TargetSingleComp targetSingle = targetSingleMapper.get(entityId);
            if(targetSingle.targetId != -1){
                PositionComp tgtPos = positionMapper.get(targetSingle.targetId);
                StainSlashComp slash = stainSlashMapper.get(entityId);

                for(int i = 0; i < cooldown.shootCount; i++){
                    damageQueue.add(entityId, targetSingle.targetId, DamageQueue.newData(DamageQueue.SlashTileStainData.class).pos(tgtPos.tileX(), tgtPos.tileY()).dmg(slash.damage, slash.range));
                }
            }
        }

    }
}
