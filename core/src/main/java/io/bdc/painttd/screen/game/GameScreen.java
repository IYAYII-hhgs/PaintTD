package io.bdc.painttd.screen.game;

import com.badlogic.gdx.*;
import io.bdc.painttd.*;
import io.bdc.painttd.screen.mainmenu.*;
import io.bdc.painttd.world.*;

public class GameScreen implements Screen {
    public PaintTD app;
    public WorldRuntime currentWorld;

    public GameHud hud;
    public PauseWindow pauseWindow;
    public GameScreenInputAdapter inputAdapter;

    public GameScreen(PaintTD app) {
        this.app = app;
    }

    @Override
    public void show() {
        app.worldView.reset();
        currentWorld = new WorldRuntime(app, app.worldView);
        WorldConfiguration assembler = new WorldConfiguration();
        assembler.assemble(currentWorld);

        hud = new GameHud(this, currentWorld);
        pauseWindow = new PauseWindow(this);
        app.ui.mainLayer.addActor(hud.root);

        inputAdapter = new GameScreenInputAdapter(this);
        app.input.setScreenInput(inputAdapter);
        app.currentWorld = currentWorld;
        hud.refresh();
    }

    @Override
    public void render(float delta) {
        if (currentWorld == null) {
            return;
        }

        currentWorld.runFrame(delta);
        if (hud != null) {
            hud.refresh();
        }
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
        app.currentWorld = null;
        app.input.setScreenInput(null);

        if (hud != null && hud.root != null) {
            hud.root.remove();
        }

        if (pauseWindow != null && app.ui.containsWindow(pauseWindow)) {
            app.ui.removeWindow(pauseWindow);
        }
    }

    @Override
    public void dispose() {
        if (currentWorld != null) {
            currentWorld.dispose();
            currentWorld = null;
        }

        hud = null;
        pauseWindow = null;
        inputAdapter = null;
    }

    public boolean togglePauseWindow() {
        if (pauseWindow == null) {
            return false;
        }

        if (app.ui.containsWindow(pauseWindow)) {
            app.ui.removeWindow(pauseWindow);
            return true;
        }

        centerPauseWindow();
        app.ui.pushWindow(pauseWindow);
        return true;
    }

    public void resumeFromPause() {
        if (pauseWindow != null) {
            app.ui.removeWindow(pauseWindow);
        }
    }

    public void returnToMenu() {
        app.setScreen(new MainMenuScreen(app));
    }

    private void centerPauseWindow() {
        pauseWindow.pack();
        float x = Math.max(0f, (app.ui.stage.getWidth() - pauseWindow.getWidth()) * 0.5f);
        float y = Math.max(0f, (app.ui.stage.getHeight() - pauseWindow.getHeight()) * 0.5f);
        pauseWindow.setPosition(x, y);
    }
}
