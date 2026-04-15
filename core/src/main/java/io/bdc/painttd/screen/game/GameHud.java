package io.bdc.painttd.screen.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.bdc.painttd.content.*;
import io.bdc.painttd.lib.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.assemble.step.post.*;
import io.bdc.painttd.world.store.*;

import java.util.*;

public class GameHud {
    public GameScreen screen;
    public WorldRuntime world;

    public Table root;
    public Label fpsLabel;
    public Label worldLabel;
    public Label worldStateLabel;

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

        worldStateLabel = new Label("", screen.app.skins.skin);
        worldStateLabel.setWrap(true);

        Table buttonsTable = new Table();
        buttonsTable.defaults().growX();

        TextButton buttonPause = ActorUtils.wrap(new TextButton("暂停", screen.app.skins.skin))
                                .click(b -> screen.togglePauseWindow())
                                .free();

        TextButton buttonTileBucketDebug = ActorUtils.wrap(new TextButton("打开TileBucket调试", screen.app.skins.skin))
                                        .click(b -> screen.toggleTileBucketDebugWindow())
                                        .free();

        TextButton buttonTestUnit = ActorUtils.wrap(new TextButton("放置测试单位", screen.app.skins.skin))
                                .click(b -> screen.placement.toggle(Entities.test))
                                .free();

        TextButton buttonTestBuilding = ActorUtils.wrap(new TextButton("放置测试建筑", screen.app.skins.skin))
                                        .click(b -> screen.placement.toggle(Entities.testBuilding))
                                        .free();

        TextButton buttonTestInitVelocityStep = ActorUtils.wrap(new TextButton("放置附加初始速度", screen.app.skins.skin))
                                            .click(b -> screen.placement.addPostSteps(new PostTestRandomVelocityStep().set(0.1f, 0.1f)))
                                            .free();

        TextButton buttonTestClearSteps = ActorUtils.wrap(new TextButton("清除放置附加步骤", screen.app.skins.skin))
                                                .click(b -> screen.placement.clearPostSteps())
                                                .free();

        TextButton buttonTestUnit5000 = ActorUtils.wrap(new TextButton("随机放置5000个实体", screen.app.skins.skin))
                                            .click(b -> {
                                                if (screen.placement.select == null) return;
                                                for (int i = 0; i < 5000; i++) {
                                                    screen.placement.place(MathUtils.random(0f, world.getStore(MapStore.class).width), MathUtils.random(0f, world.getStore(MapStore.class).height));
                                                }
                                            })
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
                                         world.getStore(DestroyQueue.class).add(target);
                                     }
                                 })
                                 .free();

        TextButton buttonDeleteAll = ActorUtils.wrap(new TextButton("删除所有单位", screen.app.skins.skin))
                                      .click(b -> {
                                          var store = world.getStore(DestroyQueue.class);
                                          for (int i = 0; i < world.getStore(EntityMetaStore.class).size(); i++) {
                                              store.add(world.getStore(EntityMetaStore.class).eidOf(i));
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
        buttonsTable.add(buttonTestInitVelocityStep).row();
        buttonsTable.add(buttonTestClearSteps).row();
        buttonsTable.add(buttonTestUnit5000).row();
        buttonsTable.add(buttonTestBuilding).row();
        buttonsTable.add(buttonDelete).row();
        buttonsTable.add(buttonDeleteAll).row();

        panel.add(buttonsTable).left();
        panel.row();
        panel.add(buttonTileBucketDebug).left();
        panel.row();
        panel.add(buttonPause).left();
        panel.row();
        panel.add(worldStateLabel).width(480f).left();

        hudRoot.add(panel).expand().top().left().pad(12f);
        return hudRoot;
    }

    public void refresh() {
        if (fpsLabel == null || worldLabel == null || world == null) {
            return;
        }

        fpsLabel.setText(String.format(
            Locale.ROOT,
            "FPS: %d RAM: %.2f/%.2f",
            Gdx.graphics.getFramesPerSecond(),
            Gdx.app.getJavaHeap() / 1e6,
            Gdx.app.getNativeHeap() / 1e6
        ));

        worldLabel.setText(String.format(
                Locale.ROOT,
                "Tick: %d Time: %.2f Entity Count: %d\nEsc opens pause window",
                world.tick,
                world.time,
                world.getStore(EntityMetaStore.class).size()
        ));

        StringBuilder entityString = new StringBuilder("调试状态");
        entityString.append("\n生成队列: ").append(world.getStore(SpawnRequestQueue.class).lastSpawn);
        entityString.append("\n销毁队列: ").append(world.getStore(DestroyQueue.class).lastDestroy);
        entityString.append("\n放置器附加步骤数量: ").append(screen.placement.extraSteps.size);
        var collisionDebug = world.getStore(CollisionDebugStore.class);
        entityString.append("\nEE查询: ").append(collisionDebug.eeQueryCount);
        entityString.append("\nEE候选: ").append(collisionDebug.eeCandidateCount);
        entityString.append("\nEE精筛命中: ").append(collisionDebug.eeOverlapCount);
        entityString.append("\nEE无向唯一对: ").append(collisionDebug.eeUniquePairCount);
        entityString.append("\nEC墙格命中: ").append(collisionDebug.ecWallCount);

        worldStateLabel.setText(String.format(
            Locale.ROOT,
            entityString.toString()
        ));

        if (screen.tileBucketDebugWindow != null && screen.app.ui.containsWindow(screen.tileBucketDebugWindow)) {
            screen.tileBucketDebugWindow.refresh();
        }
    }
}
