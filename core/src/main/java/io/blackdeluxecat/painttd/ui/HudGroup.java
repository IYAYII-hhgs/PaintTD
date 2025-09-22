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
import io.blackdeluxecat.painttd.utils.func.*;

import static io.blackdeluxecat.painttd.Core.*;
import static io.blackdeluxecat.painttd.game.Game.*;
import static io.blackdeluxecat.painttd.ui.Styles.*;

public class HudGroup extends WidgetGroup{
    //placement brush
    public MapEditBrush current;

    public PlacementFragment placement;
    public ColorFragment colors;

    public MapEditorFragment mapEditorTable;

    public HudGroup(){
        placement = new PlacementFragment();
        colors = new ColorFragment();

        mapEditorTable = new MapEditorFragment();
    }

    public void rebuild(){
        clear();
        setFillParent(true);
        ActorUtils.setVisible(this, g -> Vars.inGame);
        stage.addActor(this);

        fill(t -> {
            t.setName("info");
            t.right().top();
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
            t.setName("placement");
            t.left();
            placement.rebuild();
            t.add(placement).growY();
        });

        //Color, Menu
        fill(t -> {
            t.setName("color and menu");
            t.bottom();
            t.defaults().bottom();
            colors.rebuild();
            t.add(colors).growX().bottom();

            mapEditorTable.rebuild();
            t.add(mapEditorTable);

            Table menu = new Table();
            menu.setName("menu");
            t.add(menu).fill();
            menu.defaults().growY().width(3 * buttonSize);

            menu.add(ActorUtils.wrapper
                         .set(new TextButton("取消", sTextB))
                         .click(b -> {
                             current = null;
                         }).update(b -> b.setVisible(current != null)).actor);

            menu.add(ActorUtils.wrapper
                         .set(new TextButton("存档?", sTextB))
                         .click(b -> {
                             SaveHandler.save("save0");
                         }).actor);

            menu.add(ActorUtils.wrapper
                         .set(new TextButton("退出", sTextB))
                         .click(b -> {
                             Vars.inGame = false;
                             Vars.inMenu = true;
                             Game.endMap();
                         }).actor);
        }).bottom().left();
    }

    public Table fill(Cons<Table> cons){
        Table table = new Table();
        table.setFillParent(true);
        cons.get(table);
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
}
