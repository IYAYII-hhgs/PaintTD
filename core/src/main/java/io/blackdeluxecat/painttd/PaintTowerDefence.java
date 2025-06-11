package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class PaintTowerDefence extends ApplicationAdapter{
    private SpriteBatch batch;
    private Texture image;

    @Override
    public void create(){
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
    }

    @Override
    public void render(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();
    }

    @Override
    public void dispose(){
        batch.dispose();
        image.dispose();
    }
}
