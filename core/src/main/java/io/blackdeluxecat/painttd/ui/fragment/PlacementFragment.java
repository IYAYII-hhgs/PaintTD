package io.blackdeluxecat.painttd.ui.fragment;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.ui.*;

import static io.blackdeluxecat.painttd.ui.Styles.buttonSize;
import static io.blackdeluxecat.painttd.ui.Styles.sTextBEmpty;

public class PlacementFragment extends VerticalGroup{
    Array<EntityType> tmp = new Array<>();
    public void rebuild(){
        clear();
        setWidth(60f);
        wrap(false);
        pad(8);
        space(8);

        tmp.clear();
        tmp.addAll(EntityTypes.getByCategory(EntityTypes.cBuilding));
        //debug
        tmp.addAll(EntityTypes.getByCategory(EntityTypes.cUnit));

        for(var ttype : tmp){
            HudGroup.EntityBrush brush = new HudGroup.EntityBrush(ttype.id){
                @Override
                public void getType(){
                    type = EntityTypes.getById(ttype.id);
                }
            };

            addActor(ActorUtils.wrapper
                           .set(new TextButton(ttype.id, sTextBEmpty))
                           .with(tt -> {
                               var b = (Button)tt;
                               b.setSize(buttonSize, buttonSize);
                           })
                           .click(b -> Vars.hud.current = brush)
                           .actor);
        }
    }
}
