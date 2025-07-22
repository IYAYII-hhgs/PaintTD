package lib.ui;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lib.func.*;

public class TableUtils{
    public static <T extends Actor> void updater(T actor, Cons<T> updater){
        RunnableAction run = new RunnableAction();
        run.setRunnable(() -> updater.get(actor));
        RepeatAction repeat = new RepeatAction();
        repeat.setCount(RepeatAction.FOREVER);
        repeat.setAction(run);
        actor.addAction(repeat);
    }

    public static <T extends Actor> void clicked(T actor, Cons<T> clicker){
        actor.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                clicker.get(actor);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void clicked(InputEvent event, float x, float y){
            }
        });
    }
}
