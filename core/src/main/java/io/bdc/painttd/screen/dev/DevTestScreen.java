package io.bdc.painttd.screen.dev;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.screen.mainmenu.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class DevTestScreen implements Screen {
    public PaintTD app;
    public WorldRuntime currentWorld;
    public Table root;
    public Label runtimeLabel;
    public Label projectionLabel;
    public InputAdapter screenInput;

    public DevTestScreen(PaintTD app) {
        this.app = app;
    }

    @Override
    public void show() {
        currentWorld = new WorldRuntime(app, app.worldView);
        assembleProbeWorld(currentWorld);
        currentWorld.sortSystems();

        app.worldView.reset();

        root = buildRoot();
        app.ui.mainLayer.addActor(root);

        screenInput = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode != Keys.ESCAPE) {
                    return false;
                }

                returnToMenu();
                return true;
            }
        };
        app.input.setScreenInput(screenInput);
        app.currentWorld = currentWorld;
        refreshLabels();
    }

    @Override
    public void render(float delta) {
        currentWorld.runFrame(delta);
        refreshLabels();
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

        if (root != null) {
            root.remove();
        }
    }

    @Override
    public void dispose() {
        if (currentWorld != null) {
            currentWorld.dispose();
            currentWorld = null;
        }

        root = null;
        runtimeLabel = null;
        projectionLabel = null;
        screenInput = null;
    }

    private Table buildRoot() {
        Table table = new Table(app.skins.skin);
        table.setName("dev-test-root");
        table.setFillParent(true);

        Table content = new Table(app.skins.skin);
        content.defaults().pad(6f).left().width(520f);

        Label title = new Label("Dev Test Screen", app.skins.skin);
        Label hint = new Label(
                "Verifies WorldRuntime wiring. Press Esc or use the button below to leave this screen.",
                app.skins.skin
        );
        hint.setWrap(true);

        runtimeLabel = new Label("", app.skins.skin);
        runtimeLabel.setWrap(true);

        projectionLabel = new Label("", app.skins.skin);
        projectionLabel.setWrap(true);

        TextButton returnButton = new TextButton("Return to Main Menu", app.skins.skin);
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                returnToMenu();
            }
        });

        content.add(title);
        content.row();
        content.add(hint).fillX();
        content.row();
        content.add(runtimeLabel).fillX();
        content.row();
        content.add(projectionLabel).fillX();
        content.row();
        content.add(returnButton).left();

        table.add(content).center();
        return table;
    }

    private void refreshLabels() {
        if (currentWorld == null || runtimeLabel == null || projectionLabel == null) {
            return;
        }

        ProbeStore probe = currentWorld.getStore(ProbeStore.class);

        runtimeLabel.setText(
                "app.currentWorld active=" + (app.currentWorld != null)
                        + "\nframes=" + probe.totalFrames
                        + ", tick=" + currentWorld.tick
                        + ", lastSystems=" + probe.lastSystemsRan
                        + ", lastPhase=" + probe.lastPhase
                        + "\ntrace=" + probe.lastFrameTrace
        );

        float centerX = Gdx.graphics.getWidth() * 0.5f;
        float centerY = Gdx.graphics.getHeight() * 0.5f;
        Vector2 worldAtCenter = app.worldView.screenToWorld(centerX, centerY);
        Vector2 screenAtOrigin = app.worldView.worldToScreen(0f, 0f);
        Vector2 uiAtCenter = app.worldView.uiStageCoordinates(centerX, centerY);

        projectionLabel.setText(
                "screenToWorld(center)=" + format(worldAtCenter)
                        + "\nworldToScreen(origin)=" + format(screenAtOrigin)
                        + "\nuiStage(center)=" + format(uiAtCenter)
        );
    }

    private String format(Vector2 value) {
        return String.format("(%.1f, %.1f)", value.x, value.y);
    }

    private void assembleProbeWorld(WorldRuntime world) {
        world.addStore(new ProbeStore());

        world.addSystem(new ProbeSystem(world, WorldPhase.APPLY, 300));
        world.addSystem(new ProbeSystem(world, WorldPhase.PREPARE, 100));
        world.addSystem(new ProbeSystem(world, WorldPhase.CLEANUP, 400));
        world.addSystem(new ProbeSystem(world, WorldPhase.SPAWN, 200));
        world.addSystem(new ProbeSystem(world, WorldPhase.SIMULATE, 250));

        // Render phases stay reserved here until a later chunk provides concrete systems.
    }

    private void returnToMenu() {
        app.setScreen(new MainMenuScreen(app));
    }

    private static final class ProbeStore implements WorldStore {
        public int totalFrames;
        public int lastSystemsRan;
        public float lastDelta;
        public final StringBuilder traceBuilder = new StringBuilder();
        public String lastFrameTrace = "";
        public WorldPhase lastPhase;
    }

    private static final class ProbeSystem extends WorldSystem {
        public ProbeStore probe;

        private ProbeSystem(WorldRuntime world, WorldPhase phase, int order) {
            super(world, phase, order);
        }

        @Override
        public void onBind(WorldAccess binder) {
            probe = binder.getStore(ProbeStore.class);
        }

        @Override
        public void run(float delta) {
            if (phase == WorldPhase.PREPARE) {
                probe.lastSystemsRan = 0;
                probe.traceBuilder.setLength(0);
            }

            if (probe.traceBuilder.length() > 0) {
                probe.traceBuilder.append(" -> ");
            }

            probe.traceBuilder.append(phase.name());
            probe.lastPhase = phase;
            probe.lastSystemsRan += 1;
            probe.lastDelta = delta;

            if (phase == WorldPhase.CLEANUP) {
                probe.totalFrames += 1;
                probe.lastFrameTrace = probe.traceBuilder.toString();
            }
        }
    }
}
