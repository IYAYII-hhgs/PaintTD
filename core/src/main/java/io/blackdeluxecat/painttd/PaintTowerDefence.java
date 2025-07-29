package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;
import io.blackdeluxecat.painttd.ui.*;

import static io.blackdeluxecat.painttd.Core.assets;
import static io.blackdeluxecat.painttd.Core.stage;
import static io.blackdeluxecat.painttd.Vars.*;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class PaintTowerDefence extends ApplicationAdapter{

    @Override
    public void create(){
        Gdx.input.setInputProcessor(stage);

        Fonts.load();
        assets.finishLoading();
        Styles.load();

        hud.create();
        stage.addActor(hud.group);
    }

    @Override
    public void render(){
        world.act();
        stage.act();
        renderer.draw();
        stage.draw();
    }

    @Override
    public void dispose(){
        Core.atlas.dispose();
        Core.assets.dispose();
    }

    @Override
    public void resize(int width, int height){
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void resume(){
        super.resume();
    }

    @Override
    public void pause(){
        super.pause();
    }
}
