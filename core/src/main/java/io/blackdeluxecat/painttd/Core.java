package io.blackdeluxecat.painttd;

import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;

public class Core{
    public static AssetManager assets = new AssetManager();
    public static TextureAtlas atlas = new TextureAtlas("PaintTDAssets.atlas");
    public static SpriteBatch batch = new SpriteBatch();

    public static Stage stage = new Stage(new ScalingViewport(Scaling.fit, 1280, 960));

    public static ShapeRenderer shaper = new ShapeRenderer();
}
