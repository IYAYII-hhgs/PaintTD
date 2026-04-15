package io.bdc.painttd.screen.game;

import com.badlogic.gdx.*;
import io.bdc.painttd.*;
import io.bdc.painttd.infra.*;
import io.bdc.painttd.screen.mainmenu.*;
import io.bdc.painttd.world.*;

public class GameScreen implements Screen {
    public PaintTD app;
    public WorldRuntime currentWorld;
    public PlacementControl placement;

    public GameHud hud;
    public PauseWindow pauseWindow;
    public TileBucketDebugWindow tileBucketDebugWindow;
    public GameScreenInputAdapter inputAdapter;

    public GameScreen(PaintTD app) {
        this.app = app;
    }

    @Override
    public void show() {
        app.worldView.reset();
        currentWorld = new WorldRuntime(app, app.worldView);
        WorldConfiguration assembler = new WorldConfiguration().setupMap(200, 160);
        assembler.assemble(currentWorld);

        placement = new PlacementControl(currentWorld);

        hud = new GameHud(this, currentWorld);
        pauseWindow = new PauseWindow(this);
        tileBucketDebugWindow = new TileBucketDebugWindow(this, currentWorld);
        app.ui.mainLayer.addActor(hud.root);

        inputAdapter = new GameScreenInputAdapter(this);
        app.input.setScreenInput(inputAdapter);
        app.currentWorld = currentWorld;
        hud.refresh();

        app.worldView.camera.position.set(assembler.mapWidth * RenderHub.scl / 2f, assembler.mapHeight * RenderHub.scl / 2f, 0f);
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
        if (tileBucketDebugWindow != null && app.ui.containsWindow(tileBucketDebugWindow)) {
            app.ui.removeWindow(tileBucketDebugWindow);
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
        tileBucketDebugWindow = null;
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

    public boolean toggleTileBucketDebugWindow() {
        if (tileBucketDebugWindow == null) {
            return false;
        }

        if (app.ui.containsWindow(tileBucketDebugWindow)) {
            app.ui.removeWindow(tileBucketDebugWindow);
            return true;
        }

        centerWindow(tileBucketDebugWindow);
        app.ui.pushWindow(tileBucketDebugWindow);
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
        centerWindow(pauseWindow);
    }

    private void centerWindow(com.badlogic.gdx.scenes.scene2d.ui.Window window) {
        window.pack();
        float x = Math.max(0f, (app.ui.stage.getWidth() - window.getWidth()) * 0.5f);
        float y = Math.max(0f, (app.ui.stage.getHeight() - window.getHeight()) * 0.5f);
        window.setPosition(x, y);
    }
}
