package io.bdc.painttd.screen.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.bdc.painttd.world.*;

import java.util.*;

public class GameHud {
    public GameScreen screen;
    public WorldRuntime world;
    public Table root;
    public Label fpsLabel;
    public Label worldLabel;

    public GameHud(GameScreen screen, WorldRuntime world) {
        this.screen = screen;
        this.world = world;
        root = build();
    }

    private Table build() {
        Table hudRoot = new Table(screen.app.skins.skin);
        hudRoot.setName("game-hud-root");
        hudRoot.setFillParent(true);

        Table panel = new Table(screen.app.skins.skin);
        panel.defaults().left().pad(4f);

        fpsLabel = new Label("", screen.app.skins.skin);
        worldLabel = new Label("", screen.app.skins.skin);
        worldLabel.setWrap(true);

        panel.add(new Label("Game Screen", screen.app.skins.skin)).left();
        panel.row();
        panel.add(fpsLabel).left();
        panel.row();
        panel.add(worldLabel).width(260f).left();

        hudRoot.add(panel).expand().top().left().pad(12f);
        return hudRoot;
    }

    public void refresh() {
        if (fpsLabel == null || worldLabel == null || world == null) {
            return;
        }

        boolean pauseOpen = screen.pauseWindow != null && screen.app.ui.containsWindow(screen.pauseWindow);
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        worldLabel.setText(String.format(
                Locale.ROOT,
                "tick=%d time=%.2f\nEsc opens pause window\npauseWindow=%s",
                world.tick,
                world.time,
                pauseOpen ? "open" : "closed"
        ));
    }
}
