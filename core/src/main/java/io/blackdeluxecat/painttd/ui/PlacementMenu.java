package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.blackdeluxecat.painttd.game.content.*;
import io.blackdeluxecat.painttd.lib.ui.*;

import static io.blackdeluxecat.painttd.ui.Styles.*;

public class PlacementMenu{
    public void build(Table table){
        table.defaults().height(buttonSize * 2).pad(2).minWidth(buttonSize);

        Label label = new Label("调试栏", sLabel);
        table.add(label);
        table.add(new ActorUtils<>(new Table()).with(t -> {
            t.add(ActorUtils.wrapper
                          .set(new TextButton("EXIT", sTextB))
                          .click(b -> Gdx.app.exit())
                          .actor);

            t.add(ActorUtils.wrapper
                          .set(new TextButton("测试添加敌人", sTextB))
                          .click(b -> {
                              EntityTypes.debug.create();
                          })
                          .actor);
        }).actor).growX();
    }
}
