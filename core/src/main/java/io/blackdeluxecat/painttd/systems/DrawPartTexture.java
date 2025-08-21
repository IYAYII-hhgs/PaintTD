package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.graphics.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.render.*;

import static io.blackdeluxecat.painttd.Core.batch;

public class DrawPartTexture extends IteratingSystem{
    public ComponentMapper<PartTextureComp> partTextureMapper;
    public ComponentMapper<PositionComp> positionMapper;
    public ComponentMapper<HitboxComp> hitboxMapper;

    public DrawPartTexture(){
        super(Aspect.all(PartTextureComp.class, PositionComp.class));
    }

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        partTextureMapper = world.getMapper(PartTextureComp.class);
        positionMapper = world.getMapper(PositionComp.class);
        hitboxMapper = world.getMapper(HitboxComp.class);
    }

    @Override
    protected void process(int entityId){
        PartTextureComp partTextureComp = partTextureMapper.get(entityId);

//        float w = partTextureComp.atlasRegion.getRegionWidth();
//        float h = partTextureComp.atlasRegion.getRegionHeight();
//
//        if(hitboxMapper.has(entityId)){
//            w = hitboxMapper.get(entityId).width;
//            h = hitboxMapper.get(entityId).height;
//        }

        PositionComp positionComp = positionMapper.get(entityId);

        //batch.setColor(Color.WHITE);
        partTextureComp.drawable.draw(batch, positionComp.x - 0.5f, positionComp.y - 0.5f, 1, 1);
    }
}
