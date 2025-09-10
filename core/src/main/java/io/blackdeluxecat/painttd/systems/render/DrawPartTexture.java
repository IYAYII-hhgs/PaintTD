package io.blackdeluxecat.painttd.systems.render;

import com.artemis.*;
import com.artemis.systems.*;
import com.badlogic.gdx.graphics.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.logic.physics.*;
import io.blackdeluxecat.painttd.content.components.render.*;

import static io.blackdeluxecat.painttd.Core.*;

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
        batch.setColor(Color.WHITE);

        PartTextureComp partTextureComp = partTextureMapper.get(entityId);
        PositionComp positionComp = positionMapper.get(entityId);
        HitboxComp hitboxComp = hitboxMapper.get(entityId);

        boolean hasSize = hitboxComp != null;
        float w = hasSize ? hitboxComp.width : 1;
        float h = hasSize ? hitboxComp.height : 1;

        batch.setColor(Color.WHITE);
        //渲染宽高比非1的实体可能出问题
        partTextureComp.drawable.draw(batch, positionComp.x - w / 2f, positionComp.y - h / 2f, w / 2f, h / 2f, w, h, 1, 1, 0);
    }
}
