package io.bdc.painttd.infra;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;

/**
 * 弹出Window请使用pushWindow方法
 */
public class UI {
    public ScreenViewport viewport;
    public Stage stage;

    public Table root;
    public Table mainLayer;
    public Table windowLayer;
    public Table overlayLayer;
    public Array<Window> windowStack;

    public UI() {
    }

    public void create() {
        windowStack = new Array<>();

        viewport = new ScreenViewport();
        stage = new Stage(viewport);

        root = new Table();
        root.setName("ui-root");
        root.setFillParent(true);

        mainLayer = new Table();
        mainLayer.setName("ui-main-layer");
        mainLayer.setFillParent(true);

        windowLayer = new Table();
        windowLayer.setName("ui-window-layer");
        windowLayer.setFillParent(true);

        overlayLayer = new Table();
        overlayLayer.setName("ui-overlay-layer");
        overlayLayer.setFillParent(true);

        stage.addActor(root);
        root.addActor(mainLayer);
        root.addActor(windowLayer);
        root.addActor(overlayLayer);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void clearSceneLayers() {
        mainLayer.clearChildren();
        windowLayer.clearChildren();
        windowStack.clear();
    }

    public void pushWindow(Window window) {
        if (window == null) {
            return;
        }

        if (window.getParent() != windowLayer) {
            if (window.getParent() != null) {
                window.remove();
            }
            windowLayer.addActor(window);
        }

        window.toFront();
        windowStack.removeValue(window, true);
        windowStack.add(window);
    }

    public boolean containsWindow(Window window) {
        return window != null && windowStack.contains(window, true);
    }

    public void removeWindow(Window window) {
        if (window == null) {
            return;
        }

        if (windowStack.removeValue(window, true)) window.remove();
    }

    public boolean closeTopWindow() {
        if (windowStack.size == 0) {
            return false;
        }

        Window top = windowStack.pop();
        top.remove();
        return true;
    }

    public void dispose() {
        stage.dispose();
    }

    public static class UiManagerInputAdapter extends InputAdapter {
        public UI ui;

        public UiManagerInputAdapter(UI ui) {
            this.ui = ui;
        }

        @Override
        public boolean keyDown(int keycode) {
            if (keycode != Input.Keys.ESCAPE) {
                return false;
            }

            return ui.closeTopWindow();
        }
    }
}
