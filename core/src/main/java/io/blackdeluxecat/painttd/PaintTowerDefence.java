package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.game.Game;
import io.blackdeluxecat.painttd.ui.*;

import static io.blackdeluxecat.painttd.Core.*;
import static io.blackdeluxecat.painttd.game.Game.*;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class PaintTowerDefence extends ApplicationAdapter{

    @Override
    public void create(){
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Gdx.input.setInputProcessor(inputMultiplexer);

        Fonts.load();
        assets.finishLoading();
        Styles.load();

        hud.create();
        stage.addActor(hud.group);

        Input.create();
        Game.create();
    }

    @Override
    public void render(){
        stage.act();
        world.process();
        stage.draw();
    }

    @Override
    public void dispose(){
        world.dispose();
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
