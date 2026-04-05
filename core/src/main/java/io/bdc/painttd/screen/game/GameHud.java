package io.bdc.painttd.screen.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.bdc.painttd.content.*;
import io.bdc.painttd.lib.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

import java.util.*;

public class GameHud {
    public GameScreen screen;
    public WorldRuntime world;

    public Table root;
    public Label fpsLabel;
    public Label worldLabel;
    public Label worldEntityLabel;

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

        worldEntityLabel = new Label("", screen.app.skins.skin);
        worldEntityLabel.setWrap(true);

        Table buttonsTable = new Table();
        buttonsTable.defaults().growX();

        TextButton buttonPause = ActorUtils.wrap(new TextButton("暂停", screen.app.skins.skin))
                                .click(b -> screen.togglePauseWindow())
                                .free();

        TextButton buttonTestUnit = ActorUtils.wrap(new TextButton("放置测试单位", screen.app.skins.skin))
                                .click(b -> screen.placement.toggle(Entities.test))
                                .free();

        TextButton buttonTestBuilding = ActorUtils.wrap(new TextButton("放置测试建筑", screen.app.skins.skin))
                                        .click(b -> screen.placement.toggle(Entities.testBuilding))
                                        .free();

        TextButton buttonCore = ActorUtils.wrap(new TextButton("放置核心", screen.app.skins.skin))
                                            .click(b -> screen.placement.toggleCore())
                                            .free();

        TextButton buttonWall = ActorUtils.wrap(new TextButton("放置墙壁", screen.app.skins.skin))
                                    .click(b -> screen.placement.toggleWall())
                                    .free();

        TextButton buttonDelete = ActorUtils.wrap(new TextButton("删除一个单位", screen.app.skins.skin))
                                 .click(b -> {
                                     int target = -1;
                                     var store = world.getStore(EntityMetaStore.class);
                                     if (store.size() > 0) {
                                         target = store.eidOf(0);
                                     }
                                     if (target != -1) {
                                         world.getStore(DestroyRequestQueue.class).add(target);
                                     }
                                 })
                                 .free();

        panel.add(new Label("Game Screen", screen.app.skins.skin)).left();
        panel.row();
        panel.add(fpsLabel).left();
        panel.row();
        panel.add(worldLabel).width(260f).left();
        panel.row();

        buttonsTable.add(buttonCore).row();
        buttonsTable.add(buttonWall).row();
        buttonsTable.add(buttonTestUnit).row();
        buttonsTable.add(buttonTestBuilding).row();

        panel.add(buttonsTable).left();
        panel.row();
        panel.add(buttonPause).left();
        panel.row();
        panel.add(worldEntityLabel).width(480f).left();

        hudRoot.add(panel).expand().top().left().pad(12f);
        return hudRoot;
    }

    public void refresh() {
        if (fpsLabel == null || worldLabel == null || world == null) {
            return;
        }

        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        worldLabel.setText(String.format(
                Locale.ROOT,
                "Tick: %d Time: %.2f Entity Count: %d\nEsc opens pause window",
                world.tick,
                world.time,
                world.getStore(EntityMetaStore.class).size()
        ));

        var metaStore = world.getStore(EntityMetaStore.class);
        StringBuilder entityString = new StringBuilder("EntityMetaArray\n");
        for (int i = 0; i < 5; i++) {
            if (i >= metaStore.size()) break;
            entityString.append(metaStore.indexer.eidOf(i));
            entityString.append("\n");
        }

        worldEntityLabel.setText(String.format(
            Locale.ROOT,
            entityString.toString()
        ));
    }
}
