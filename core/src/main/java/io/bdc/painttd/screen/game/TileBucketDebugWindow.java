package io.bdc.painttd.screen.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.infra.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.api.*;
import io.bdc.painttd.world.store.*;

import java.util.*;

public class TileBucketDebugWindow extends Window {
    public GameScreen screen;
    public WorldRuntime world;

    public TileBucketDebugStore debugStore;
    public TileBucketStore bucketStore;
    public TileBucketQueryAPI queryApi;

    public Label bucketNodesLabel;
    public Label occupiedCellsLabel;
    public Label coveredEntitiesLabel;
    public Label maxBucketLoadLabel;
    public Label hoverEidLabel;
    public Label hoverCellLabel;
    public Label hoverCellQueryLabel;
    public Label hoverSquareQueryLabel;
    public TextField hoverSquareSizeField;

    private final Vector2 mouseWorld = new Vector2();
    private final Vector2 entityPos = new Vector2();

    public TileBucketDebugWindow(GameScreen screen, WorldRuntime world) {
        super("TileBucket Debug", screen.app.skins.skin);
        this.screen = screen;
        this.world = world;
        this.debugStore = world.getStore(TileBucketDebugStore.class);
        this.bucketStore = world.getStore(TileBucketStore.class);
        this.queryApi = world.getApi(TileBucketQueryAPI.class);

        setName("tilebucket-debug-window");
        setModal(false);
        setMovable(true);
        setResizable(false);

        build();
    }

    private void build() {
        Table cont = new Table();

        CheckBox overlayToggle = new CheckBox("显示全局格子桶覆盖", screen.app.skins.skin);
        overlayToggle.setChecked(debugStore.showBucketOverlay);
        overlayToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                debugStore.showBucketOverlay = overlayToggle.isChecked();
            }
        });

        CheckBox selectedToggle = new CheckBox("显示悬停实体覆盖", screen.app.skins.skin);
        selectedToggle.setChecked(debugStore.showSelectedCoverage);
        selectedToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                debugStore.showSelectedCoverage = selectedToggle.isChecked();
            }
        });

        CheckBox hoverCellToggle = new CheckBox("显示悬停格查询", screen.app.skins.skin);
        hoverCellToggle.setChecked(debugStore.showHoverCellQuery);
        hoverCellToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                debugStore.showHoverCellQuery = hoverCellToggle.isChecked();
            }
        });

        CheckBox hoverSquareToggle = new CheckBox("显示悬停方形查询", screen.app.skins.skin);
        hoverSquareToggle.setChecked(debugStore.showHoverSquareQuery);
        hoverSquareToggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                debugStore.showHoverSquareQuery = hoverSquareToggle.isChecked();
            }
        });

        hoverSquareSizeField = new TextField(String.format(Locale.ROOT, "%.1f", debugStore.hoverSquareSize), screen.app.skins.skin);
        hoverSquareSizeField.setTextFieldFilter((field, c) -> Character.isDigit(c) || c == '.');
        hoverSquareSizeField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tryApplyHoverSquareSize();
            }
        });

        Table squareParamTable = new Table(screen.app.skins.skin);
        squareParamTable.defaults().left().padRight(6f);
        squareParamTable.add(new Label("方形查询尺寸", screen.app.skins.skin));
        squareParamTable.add(hoverSquareSizeField).width(96f);

        bucketNodesLabel = new Label("", screen.app.skins.skin);
        occupiedCellsLabel = new Label("", screen.app.skins.skin);
        coveredEntitiesLabel = new Label("", screen.app.skins.skin);
        maxBucketLoadLabel = new Label("", screen.app.skins.skin);
        hoverEidLabel = new Label("", screen.app.skins.skin);
        hoverCellLabel = new Label("", screen.app.skins.skin);
        hoverCellQueryLabel = new Label("", screen.app.skins.skin);
        hoverCellQueryLabel.setWrap(true);
        hoverSquareQueryLabel = new Label("", screen.app.skins.skin);
        hoverSquareQueryLabel.setWrap(true);

        cont.defaults().pad(2f).left();
        cont.add(overlayToggle);
        cont.row();
        cont.add(selectedToggle);
        cont.row();
        cont.add(hoverCellToggle);
        cont.row();
        cont.add(hoverSquareToggle);
        cont.row();
        cont.defaults().growX();
        cont.add(squareParamTable).width(360f);
        cont.row();
        cont.add(bucketNodesLabel).width(360f);
        cont.row();
        cont.add(occupiedCellsLabel).width(360f);
        cont.row();
        cont.add(coveredEntitiesLabel).width(360f);
        cont.row();
        cont.add(maxBucketLoadLabel).width(360f);
        cont.row();
        cont.add(hoverEidLabel).width(360f);
        cont.row();
        cont.add(hoverCellLabel).width(360f);
        cont.row();
        cont.add(hoverCellQueryLabel).width(360f);
        cont.row();
        cont.add(hoverSquareQueryLabel).width(360f);

        add(cont).top().pad(4f).minSize(360f, 600f);
        pack();
    }

    public void refresh() {
        if (bucketStore == null || debugStore == null) {
            return;
        }

        int occupiedCells = 0;
        int maxBucketLoad = 0;
        for (int cell = 0; cell < bucketStore.cellCount(); cell++) {
            int load = 0;
            for (int node = bucketStore.headByCell[cell]; node != -1; node = bucketStore.nextInCell[node]) {
                load += 1;
            }
            if (load > 0) {
                occupiedCells += 1;
                maxBucketLoad = Math.max(maxBucketLoad, load);
            }
        }

        int coveredEntities = 0;
        for (int eid = 0; eid < bucketStore.firstNodeByEid.length; eid++) {
            if (bucketStore.firstNodeByEid[eid] != -1) {
                coveredEntities += 1;
            }
        }

        bucketNodesLabel.setText("桶节点数: " + bucketStore.activeNodeCount());
        maxBucketLoadLabel.setText("桶节点历史最值: " + maxBucketLoad);
        occupiedCellsLabel.setText("网格总数: " + occupiedCells);
        coveredEntitiesLabel.setText("实体总数: " + coveredEntities);

        if (!screen.app.worldView.canHandleWorldInput()) {
            hoverEidLabel.setText("悬停处实体: <ui-hit>");
            hoverCellLabel.setText("悬停处网格: <ui-hit>");
            hoverCellQueryLabel.setText("悬停处网格查询: <disabled>");
            hoverSquareQueryLabel.setText(String.format(Locale.ROOT,
                    "悬停处方形查询(size=%.1f): <disabled>",
                    debugStore.hoverSquareSize));
            return;
        }

        screen.app.worldView.screenToWorld(Gdx.input.getX(), Gdx.input.getY(), mouseWorld);
        float worldX = mouseWorld.x / RenderHub.scl;
        float worldY = mouseWorld.y / RenderHub.scl;
        int cellX = MathUtils.floor(worldX);
        int cellY = MathUtils.floor(worldY);
        if (!bucketStore.inBounds(cellX, cellY)) {
            hoverEidLabel.setText("悬停处实体: <out-of-bounds>");
            hoverCellLabel.setText("悬停处网格: <out-of-bounds>");
            hoverCellQueryLabel.setText("悬停处网格查询: <out-of-bounds>");
            hoverSquareQueryLabel.setText(String.format(Locale.ROOT,
                    "悬停处方形查询(size=%.1f): <out-of-bounds>",
                    debugStore.hoverSquareSize));
            return;
        }

        int hoverCell = bucketStore.index(cellX, cellY);
        hoverCellLabel.setText("悬停处网格: (" + cellX + ", " + cellY + ") cid=" + hoverCell);

        IntArray cellResults = queryApi.collectCell(hoverCell);
        int hoverEid = findNearestEntity(cellResults, worldX, worldY);
        hoverEidLabel.setText("悬停处实体: " + (hoverEid >= 0 ? hoverEid : "<none>"));

        if (debugStore.showHoverCellQuery) {
            hoverCellQueryLabel.setText("悬停处网格查询: " + cellResults.size + " -> " + summarize(cellResults));
        } else {
            hoverCellQueryLabel.setText("悬停处网格查询: <off>");
        }

        if (debugStore.showHoverSquareQuery) {
            IntArray squareResults = queryApi.collectSquare(worldX, worldY, debugStore.hoverSquareSize);
            hoverSquareQueryLabel.setText(String.format(
                    Locale.ROOT,
                    "悬停处方形查询(size=%.1f): %d -> %s",
                    debugStore.hoverSquareSize,
                    squareResults.size,
                    summarize(squareResults)
            ));
        } else {
            hoverSquareQueryLabel.setText(String.format(Locale.ROOT,
                    "H悬停处方形查询(size=%.1f): <off>",
                    debugStore.hoverSquareSize));
        }
    }

    private String summarize(IntArray values) {
        if (values.size == 0) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder("[");
        int shown = Math.min(values.size, 10);
        for (int i = 0; i < shown; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(values.get(i));
        }
        if (values.size > shown) {
            builder.append(", ...");
        }
        builder.append(']');
        return builder.toString();
    }

    private int findNearestEntity(IntArray candidates, float worldX, float worldY) {
        TransformStore transformStore = world.getStore(TransformStore.class);
        int bestEid = -1;
        float bestDst2 = Float.MAX_VALUE;
        for (int i = 0; i < candidates.size; i++) {
            int eid = candidates.get(i);
            if (transformStore.get(eid, entityPos) == null) {
                continue;
            }

            float dx = entityPos.x - worldX;
            float dy = entityPos.y - worldY;
            float dst2 = dx * dx + dy * dy;
            if (dst2 < bestDst2) {
                bestDst2 = dst2;
                bestEid = eid;
            }
        }
        return bestEid;
    }

    private void tryApplyHoverSquareSize() {
        String text = hoverSquareSizeField.getText();
        if (text == null || text.isBlank()) {
            return;
        }

        try {
            float value = Float.parseFloat(text);
            if (value > 0f) {
                debugStore.hoverSquareSize = value;
            }
        } catch (NumberFormatException ignored) {
        }
    }
}
