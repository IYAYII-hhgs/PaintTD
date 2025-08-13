package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.game.Game;

import java.util.function.*;

import static io.blackdeluxecat.painttd.Core.*;
import static io.blackdeluxecat.painttd.game.Game.groups;
import static io.blackdeluxecat.painttd.ui.Styles.*;

public class Hud{
    public @Null EntityType select;
    public WidgetGroup group;

    public Table buttons = new Table();

    public void create(){
        group = new WidgetGroup();
        group.setFillParent(true);
        stage.addActor(group);
        stage.setDebugAll(true);

        fill(t -> {
            t.left().top();
            t.defaults().left();
            t.add(new Label("Paint Tower Defence", sLabel)).row();
            t.add(new Label("1", sLabel){
                @Override
                public void draw(Batch batch, float parentAlpha){
                    this.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + ", RAM: " + (int)(Gdx.app.getJavaHeap() / 1000000f) + "MB");
                    super.draw(batch, parentAlpha);
                }
            }).row();
            t.add(new ActorUtils<>(new Label("", sLabel)).update(l -> l.setText("G|Unit: " + groups.getEntities("unit").size())).actor).row();
            t.add(new ActorUtils<>(new Label("", sLabel)).update(l -> l.setText("G|Building: " + groups.getEntities("building").size())).actor);
        });

        //Placement
        fill(t -> {
            buttons.defaults().height(buttonSize * 2).pad(2).minWidth(buttonSize);

            buttons.add(new ActorUtils<>(new Table()).with(line1 -> {
                Label label = new Label("", sLabel);
                ActorUtils.wrapper.set(label).update(l -> ((Label)l).setText("选择建筑: " + (select == null ? "" : select.name)));
                line1.add(label);

                line1.add(new ActorUtils<>(new Table()).with(t1 -> {
                    t1.add(ActorUtils.wrapper
                              .set(new TextButton("敌人", sTextB))
                              .click(b -> {
                                  select = Entities.eraser;
                              })
                              .actor);

                    t1.add(ActorUtils.wrapper
                              .set(new TextButton("基础塔", sTextB))
                              .click(b -> {
                                  select = Entities.pencil;
                              })
                              .actor);
                }).actor).growX();

            }).actor).growX().row();

            buttons.add(new ActorUtils<>(new Table()).with(line2 -> {
                line2.add(ActorUtils.wrapper
                              .set(new TextButton("重置世界", sTextB))
                              .click(b -> Game.loadMap())
                              .actor);

                line2.add(ActorUtils.wrapper
                          .set(new TextButton("退出", sTextB))
                          .click(b -> Gdx.app.exit())
                          .actor);

            }).actor).growX();

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
