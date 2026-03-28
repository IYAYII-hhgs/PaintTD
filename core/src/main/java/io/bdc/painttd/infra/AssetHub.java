package io.bdc.painttd.infra;

import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.assets.loaders.resolvers.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;

public class AssetHub {
    public static String CORE_ATLAS = "atlas/textures.atlas";

    public AssetManager manager;
    public TextureAtlas atlas;

    public AssetHub() {
        manager = new AssetManager();
    }

    public AssetManager getManager() {
        return manager;
    }

    public void registerCoreLoaders() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    public void loadAtlas() {
        manager.load(CORE_ATLAS, TextureAtlas.class);
        manager.finishLoading();
        atlas = manager.get(CORE_ATLAS, TextureAtlas.class);
    }

    public void dispose() {
        if (manager != null) {
            manager.dispose();
        }
        manager = null;
        atlas = null;
    }
}
