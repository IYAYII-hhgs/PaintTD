package io.blackdeluxecat.painttd.ui.fragment;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.ui.*;

import static io.blackdeluxecat.painttd.game.Game.rules;
import static io.blackdeluxecat.painttd.ui.Styles.buttonSize;
import static io.blackdeluxecat.painttd.ui.Styles.sTextBEmpty;

public class ColorFragment extends HorizontalGroup{
    public int selectColorIndex = 0;
    public void rebuild(){
        clear();
        pad(8f);
        space(2f);
        for(int i = 0; i < rules.colorPalette.colors.size; i++){
            int finalI = i;
            addActor(ActorUtils.wrapper
                         .set(new Button(sTextBEmpty))
                         .with(b -> {
                             ((Button)b).add(new Image(Styles.white)).size(buttonSize).getActor().setColor(rules.colorPalette.getColor(Vars.c1, finalI));
                         })
                         .click(b -> selectColorIndex = finalI)
                         .update(b -> {
                             ((Button)b).setChecked(selectColorIndex == finalI);
                         })
                         .actor);
        }
    }
}
