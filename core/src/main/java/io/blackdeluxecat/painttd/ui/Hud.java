package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.function.*;

import static io.blackdeluxecat.painttd.Core.stage;

public class Hud{
    public WidgetGroup group;

    public Table buttons = new Table();

    public void create(){
        group = new WidgetGroup();
        group.setFillParent(true);
        stage.addActor(group);

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

        fill(t -> {
            t.setName("placement");
            t.bottom();
            Table sel = new Table();
            sel.add(new TextButton("Pencil", new TextButton.TextButtonStyle()));
        });

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
