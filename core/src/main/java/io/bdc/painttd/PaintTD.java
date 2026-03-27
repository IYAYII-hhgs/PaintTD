package io.bdc.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.kotcrab.vis.ui.*;
import io.bdc.painttd.infra.*;
import io.bdc.painttd.infra.Input;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class PaintTD extends ApplicationAdapter {
    public static final Logger log = new Logger("Core", Logger.DEBUG);

    @Override
    public void create() {
        Assets.load();
        Render.load();
        UI.load();
        Input.load();

        UI.stage.addActor(new Label("test", VisUI.getSkin()));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        UI.stage.getViewport().apply(true);
        UI.stage.act();
        UI.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        UI.stage.getViewport().update(width, height, true);
        UI.toasts.show("Hello Orin! Test", 3);
    }

    @Override
    public void dispose() {
        UI.dispose();
        Render.dispose();
        Assets.dispose();
    }
}
