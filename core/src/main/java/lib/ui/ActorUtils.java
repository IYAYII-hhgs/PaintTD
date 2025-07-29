package lib.ui;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import lib.func.*;

public class ActorUtils{
    public static ActorUtils wrapper = new ActorUtils();

    public Actor actor;

    public ActorUtils(){}

    public ActorUtils set(Actor actor){
        this.actor = actor;
        return this;
    }

    public ActorUtils update(Cons<Actor> updater){
        ActorUtils.updater(actor, updater);
        return this;
    }

    public ActorUtils click(Cons<Actor> clicked){
        ActorUtils.clicked(actor, clicked);
        return this;
    }

    public static <T extends Actor> void updater(T actor, Cons<T> updater){
        RunnableAction run = new RunnableAction();
        run.setRunnable(() -> updater.get(actor));
        RepeatAction repeat = new RepeatAction();
        repeat.setCount(RepeatAction.FOREVER);
        repeat.setAction(run);
        actor.addAction(repeat);
    }

    public static <T extends Actor> void clicked(T actor, Cons<T> clicked){
        actor.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                clicked.get(actor);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void clicked(InputEvent event, float x, float y){
            }
        });
    }
}
