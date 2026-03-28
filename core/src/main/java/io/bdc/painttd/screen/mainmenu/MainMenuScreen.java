package io.bdc.painttd.screen.mainmenu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.bdc.painttd.*;

public class MainMenuScreen implements Screen {
    public PaintTD app;
    public MainMenu menuRoot;
    public Window aboutWindow;
    public Window controlsWindow;

    public MainMenuScreen(PaintTD app) {
        this.app = app;
    }

    @Override
    public void show() {
        menuRoot = new MainMenu(app.skins.skin);
        app.ui.mainLayer.addActor(menuRoot);
        app.input.setScreenInput(null);
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        app.currentWorld = null;// 保险清理

        if (menuRoot != null) {
            menuRoot.remove();
        }

        app.input.setScreenInput(null);
    }

    @Override
    public void dispose() {
        menuRoot = null;
        aboutWindow = null;
        controlsWindow = null;
    }
}
