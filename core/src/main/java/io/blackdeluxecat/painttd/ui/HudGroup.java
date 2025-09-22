package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.Game;
import io.blackdeluxecat.painttd.io.*;
import io.blackdeluxecat.painttd.ui.fragment.*;

import java.util.function.*;

import static io.blackdeluxecat.painttd.Core.*;
import static io.blackdeluxecat.painttd.game.Game.*;
import static io.blackdeluxecat.painttd.ui.Styles.*;

public class HudGroup extends WidgetGroup{
    public MapEditBrush current;
    public Table placement, colorPalette, mapEditor;

    public Table buttons = new Table();
    public MapEditorTable mapEditorTable;

    public void create(){
        clear();
        setFillParent(true);
        ActorUtils.setVisible(this, g -> Vars.inGame);
        stage.addActor(this);

        mapEditorTable = new MapEditorTable();

        fill(t -> {
            t.left().top();
            t.defaults().left();
            t.add(new Label("1", sLabel){
                @Override
                public void draw(Batch batch, float parentAlpha){
                    this.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + ", RAM: " + (int)(Gdx.app.getJavaHeap() / 1000000f) + "MB");
                    super.draw(batch, parentAlpha);
                }
            }).row();
        });

        //Placement
        fill(t -> {
            buttons.defaults().height(buttonSize * 2f).pad(2).minWidth(buttonSize);

            buttons.add(new ActorUtils<>(new Table()).with(line1 -> {
                Label label = new Label("", sLabel);
                ActorUtils.wrapper.set(label).update(l -> ((Label)l).setText("选择建筑: " + (current == null ? "" : current.name)));
                line1.add(label);

                line1.add(new ActorUtils<>(new Table()).with(t1 -> {
                    t1.add(ActorUtils.wrapper
                               .set(new TextButton(bEnemy.name, sTextB))
                               .click(b -> {
                                   current = bEnemy;
                               })
                               .actor);

                    t1.add(ActorUtils.wrapper
                               .set(new TextButton(bPencil.name, sTextB))
                               .click(b -> {
                                   current = bPencil;
                               })
                               .actor);

                    t1.add(ActorUtils.wrapper
                               .set(new TextButton(bBrush.name, sTextB))
                               .click(b -> {
                                   current = bBrush;
                               })
                               .actor);

                }).actor).growX();

            }).actor).growX().row();

            buttons.add(mapEditorTable).growX().row();

            t.add(buttons).growX();

            t.add(new ActorUtils<>(new Table()).with(rt -> {

                rt.defaults().growY().width(4 * buttonSize);

                rt.add(ActorUtils.wrapper
                           .set(new TextButton("取消", sTextB))
                           .click(b -> {
                               current = null;
                           }).update(b -> b.setVisible(current != null)).actor);

                rt.add(ActorUtils.wrapper
                           .set(new TextButton("存档?", sTextB))
                           .click(b -> {
                               SaveHandler.save("save0");
                           }).actor);

                rt.add(ActorUtils.wrapper
                           .set(new TextButton("退出", sTextB))
                           .click(b -> {
                               Vars.inGame = false;
                               Vars.inMenu = true;
                               Game.endMap();
                           }).actor);

            }).actor).fill();
        }).bottom().left();
    }

    public Table fill(Consumer<Table> cons){
        Table table = new Table();
        table.setFillParent(true);
        cons.accept(table);
        this.addActor(table);
        return table;
    }

    public abstract static class MapEditBrush{
        public String name;

        public MapEditBrush(String name){
            this.name = name;
        }

        public abstract void draw(float worldX, float worldY);
    }

    public abstract static class EntityBrush extends MapEditBrush{
        public EntityType type;

        public EntityBrush(String name){
            super(name);
        }

        public abstract void getType();

        @Override
        public void draw(float worldX, float worldY){
            getType();
            var e = type.create();
            if(world.getMapper(MarkerComp.PlaceSnapGrid.class).has(e)){
                worldX = Math.round(worldX);
                worldY = Math.round(worldY);
            }
            Game.utils.setPosition(e.getId(), worldX, worldY);
        }
    }

    public EntityBrush bEnemy = new EntityBrush("敌人"){
        @Override
        public void getType(){
            type = Entities.eraser;
        }
    },

    bPencil = new EntityBrush("铅笔"){
        @Override
        public void getType(){
            type = Entities.pencil;
        }
    },

    bBrush = new EntityBrush("笔刷"){
        @Override
        public void getType(){
            type = Entities.brush;
        }
    };
}
