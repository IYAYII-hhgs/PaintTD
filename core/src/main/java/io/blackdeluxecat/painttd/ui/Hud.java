package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import lib.ui.*;

import java.util.function.*;

import static io.blackdeluxecat.painttd.Core.log;
import static io.blackdeluxecat.painttd.Core.stage;
import static io.blackdeluxecat.painttd.Styles.*;

public class Hud{
    public static int debug;
    public WidgetGroup group;

    public Table buttons = new Table();

    public void create(){
        group = new WidgetGroup();
        group.setFillParent(true);
        stage.addActor(group);
        stage.setDebugAll(true);
        fill(t -> {
            t.right().top();
            t.add(new Label("Paint Tower Defence", new Label.LabelStyle(new BitmapFont(), Color.WHITE))).row();
            t.add(new Label("1", new Label.LabelStyle(new BitmapFont(), Color.WHITE)){
                @Override
                public void draw(Batch batch, float parentAlpha){
                    this.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + ", RAM: " + (int)(Gdx.app.getJavaHeap() / 1000000f) + "MB");
                    super.draw(batch, parentAlpha);
                }
            });
        });

        buttons.defaults().height(buttonSize).pad(2).minWidth(buttonSize);


        Label label = new Label("fafa", sLabel);
        TableUtils.clicked(label, bb -> label.setText("法法绝叫" + debug++));
        buttons.add(label);

        TextButton exit = new TextButton("EXIT", sTextB);
        TableUtils.clicked(exit, bb -> Gdx.app.exit());
        buttons.add(exit);

        fill(t -> t.add(buttons).growX().height(buttonSize)).bottom().left();
    }

    public Table fill(Consumer<Table> cons){
        Table table = new Table();
        table.setFillParent(true);
        cons.accept(table);
        group.addActor(table);
        return table;
    }

    public void dispose(){
        group.clear();
    }
}
