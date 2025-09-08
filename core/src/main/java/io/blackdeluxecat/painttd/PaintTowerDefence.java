package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import io.blackdeluxecat.painttd.game.Game;
import io.blackdeluxecat.painttd.ui.*;

import static io.blackdeluxecat.painttd.Core.*;
import static io.blackdeluxecat.painttd.Vars.*;
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

        Vars.hud.create();
        stage.addActor(Vars.hud.group);

        InputProcessors.create();
        Game.create();

    }

    @Override
    public void render(){
        Time.update();

        lerpZoom = MathUtils.lerp(lerpZoom, zoom, 0.1f);
        worldViewport.setUnitsPerPixel(1f / lerpZoom);
        worldViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        shaper.setProjectionMatrix(worldViewport.getCamera().combined);
        batch.setProjectionMatrix(worldViewport.getCamera().combined);
        world.process();

        stage.getViewport().apply(true);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height){
        super.resize(width, height);
        Core.stage.getViewport().update(width, height, true);
    }

    @Override
    public void resume(){
        super.resume();
    }

    @Override
    public void dispose(){
        world.dispose();
        Core.atlas.dispose();
        Core.assets.dispose();
    }

    @Override
    public void pause(){
        super.pause();
    }
}
