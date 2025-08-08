package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.game.Game;

import java.util.function.*;

import static io.blackdeluxecat.painttd.Core.*;
import static io.blackdeluxecat.painttd.ui.Styles.*;

public class Hud{
    public WidgetGroup group;

    public PlacementMenu placement = new PlacementMenu();

    public Table buttons = new Table();

    public void create(){
        group = new WidgetGroup();
        group.setFillParent(true);
        stage.addActor(group);
        stage.setDebugAll(true);
        fill(t -> {
            t.right().top();
            t.add(new Label("Paint Tower Defence", sLabel)).row();
            t.add(new Label("1", sLabel){
                @Override
                public void draw(Batch batch, float parentAlpha){
                    this.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + ", RAM: " + (int)(Gdx.app.getJavaHeap() / 1000000f) + "MB");
                    super.draw(batch, parentAlpha);
                }
            }).row();
            t.add(new ActorUtils<>(new Label("", sLabel)).update(l -> l.setText("G|BaseUnit: " + Game.groups.getEntities("BaseUnit").size())).actor);
        });

        fill(t -> {
            placement.build(buttons);
            t.add(buttons).growX();
        }).bottom().left();
    }

    public Table fill(Consumer<Table> cons){
        Table table = new Table();
        table.setFillParent(true);
        cons.accept(table);
        group.addActor(table);
        return table;
    }
}
