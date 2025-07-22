package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;

public class Core{
    public static TextureAtlas atlas = new TextureAtlas("PaintTDAssets.atlas");
    public static Stage stage = new Stage(new ScalingViewport(Scaling.fit, 1280, 960));
    public static SpriteBatch batch = new SpriteBatch();
    public static Logger log = new Logger("main"){
        {setLevel(Logger.DEBUG);}
    };
}
