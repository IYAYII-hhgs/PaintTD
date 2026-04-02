package io.bdc.painttd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.content.*;
import io.bdc.painttd.infra.*;
import io.bdc.painttd.screen.mainmenu.*;
import io.bdc.painttd.world.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class PaintTD extends ApplicationAdapter {
    public static final Logger log = new Logger("Core", Logger.DEBUG);
    public static PaintTD app;

    public AssetHub assets;
    public FontHub fonts;
    public SkinHub skins;
    public UI ui;
    public InputHub input;
    public WorldView worldView;

    public Screen currentScreen;
    public WorldRuntime currentWorld;

    @Override
    public void create() {
        app = this;

        assets = new AssetHub();
        assets.registerCoreLoaders();
        assets.loadAtlas();

        fonts = new FontHub(assets);
        fonts.loadFonts();

        //TODO skinhub块临时使用VisUI skin + 替换字体
        skins = new SkinHub(fonts);
        skins.createVisUiSkin();
        skins.patchAllFonts();

        ui = new UI();
        ui.create();

        worldView = new WorldView();
        worldView.create();

        RenderHub.load();

        input = new InputHub();
        input.create();
        input.addCoreInput(new UI.UiManagerInputAdapter(ui));
        input.addCoreInput(ui.stage);

        Entities.load();

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        if (currentScreen != null) {
            currentScreen.render(delta);
        }

        ui.stage.getViewport().apply(true);
        ui.stage.act(delta);
        ui.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (ui != null) {
            ui.resize(width, height);
        }

        if (worldView != null) {
            worldView.resize(width, height);
        }

        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    @Override
    public void pause() {
        if (currentScreen != null) {
            currentScreen.pause();
        }
    }

    @Override
    public void resume() {
        if (currentScreen != null) {
            currentScreen.resume();
        }
    }

    @Override
    public void dispose() {
        if (currentScreen != null) {
            currentScreen.hide();
            currentScreen.dispose();
            currentScreen = null;
        }

        if (input != null) {
            input.dispose();
            input = null;
        }

        RenderHub.dispose();

        currentWorld = null;
        worldView = null;

        if (ui != null) {
            ui.dispose();
            ui = null;
        }

        if (skins != null) {
            skins.dispose();
            skins = null;
        }

        if (assets != null) {
            assets.dispose();
            assets = null;
        }

        fonts = null;
        if (app == this) {
            app = null;
        }
    }

    public void setScreen(Screen next) {
        if (currentScreen != null) {
            currentScreen.hide();
            ui.clearSceneLayers();
            currentScreen.dispose();
        }

        currentScreen = next;
        if (currentScreen != null) {
            currentScreen.show();
        }
    }
}
