package io.bdc.painttd.infra;

import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.g2d.*;

public class Assets {
    public static AssetManager assetManager;
    public static TextureAtlas atlas;

    public static void load() {
        assetManager = new AssetManager();

        assetManager.load("atlas/textures.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        atlas = assetManager.get("atlas/textures.atlas", TextureAtlas.class);
    }

    public static void dispose() {
        assetManager.dispose();
    }
}
