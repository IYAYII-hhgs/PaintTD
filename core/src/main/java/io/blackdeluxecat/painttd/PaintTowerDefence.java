package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.blackdeluxecat.painttd.core.*;
import io.blackdeluxecat.painttd.render.*;

import static io.blackdeluxecat.painttd.core.Core.stage;
import static io.blackdeluxecat.painttd.Vars.*;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class PaintTowerDefence extends ApplicationAdapter{

    @Override
    public void create(){
        Table shell = new Table();
        shell.setFillParent(true);
        Table ui = new Table().center();
        ui.left().bottom();
        shell.add(ui).left();
        ui.add(new Label("Paint Tower Defence", new Label.LabelStyle(new BitmapFont(), Color.WHITE))).row();
        ui.add(new Label("1", new Label.LabelStyle(new BitmapFont(), Color.WHITE)){
            @Override
            public void draw(Batch batch, float parentAlpha){
                this.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + ", RAM: " + (int)(Gdx.app.getJavaHeap() / 1000000f) + "MB");
                super.draw(batch, parentAlpha);
            }
        });
        stage.addActor(shell);
    }

    @Override
    public void render(){
        world.act();
        renderer.draw();
        stage.draw();
    }

    @Override
    public void dispose(){
        Core.atlas.dispose();
    }

    @Override
    public void resize(int width, int height){
        super.resize(width, height);
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
