package io.blackdeluxecat.painttd.content.components.render;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.blackdeluxecat.painttd.content.components.*;

import static io.blackdeluxecat.painttd.Core.atlas;

public class PartTextureComp extends CopyableComponent{
    public String textureName;
    public TextureAtlas.AtlasRegion atlasRegion;

    public PartTextureComp(){}

    public PartTextureComp(String textureName){
        this.textureName = textureName;
        this.atlasRegion = atlas.findRegion(textureName);
    }

    @Override
    public PartTextureComp copy(CopyableComponent other){
        return this;
    }

    @Override
    protected void reset(){

    }
}
