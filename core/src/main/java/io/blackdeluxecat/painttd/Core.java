package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;
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
    public static ShapeRenderer shaper = new ShapeRenderer();

    public static Stage stage = new Stage(new ScalingViewport(Scaling.fit, 1280, 960));
    /**集成的输入处理器。任何自定义处理器自行持有、在此插入一个InputProcessor。*/
    public static InputMultiplexer inputMultiplexer = new InputMultiplexer(stage);
}
