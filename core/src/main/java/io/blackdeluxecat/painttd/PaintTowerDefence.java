package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;

import static io.blackdeluxecat.painttd.Core.stage;
import static io.blackdeluxecat.painttd.Vars.*;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class PaintTowerDefence extends ApplicationAdapter{

    @Override
    public void create(){
        Gdx.input.setInputProcessor(stage);
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
        hud.dispose();
        Core.atlas.dispose();
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
