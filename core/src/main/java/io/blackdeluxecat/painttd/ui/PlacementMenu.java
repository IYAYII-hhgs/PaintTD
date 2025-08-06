package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.game.content.*;
import io.blackdeluxecat.painttd.game.content.components.logic.*;
import io.blackdeluxecat.painttd.game.content.entitytypes.*;
import io.blackdeluxecat.painttd.lib.ui.*;

import static io.blackdeluxecat.painttd.ui.Styles.*;

public class PlacementMenu{
    @Null public BaseEntityType select;
    public InputAdapter inputPlacement = new InputAdapter(){
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button){
            if(select != null){
                var e = select.create();
                var pos = e.getComponent(PositionComp.class);
                if(pos != null){
                    pos.x = screenX;
                    pos.y = Gdx.graphics.getHeight() - screenY;
                }
                select = null;
                return true;
            }
            return false;
        }
    };

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
                          .set(new TextButton("敌人", sTextB))
                          .click(b -> {
                              select = EntityTypes.eraser;
                          })
                          .actor);

            t.add(ActorUtils.wrapper
                      .set(new TextButton("基础塔", sTextB))
                      .click(b -> {
                          select = EntityTypes.pencil;
                      })
                      .actor);
        }).actor).growX();
    }
}
