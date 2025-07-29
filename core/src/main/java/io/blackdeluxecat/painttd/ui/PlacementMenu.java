package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import lib.ui.*;

import static io.blackdeluxecat.painttd.ui.Styles.*;

public class PlacementMenu{
    public void build(Table table){
        table.defaults().height(buttonSize * 2).pad(2).minWidth(buttonSize);

        Label label = new Label("调试栏", sLabel);
        table.add(label);

        table.add(ActorUtils.wrapper.
                      set(new TextButton("EXIT", sTextB))
                      .click(bb -> Gdx.app.exit())
                      .actor).row();
    }
}
