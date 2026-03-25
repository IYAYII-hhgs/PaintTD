package io.bdc.painttd.lib;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.lib.func.*;

/**
 * Actor元素的包装器, 提供更多链式调用.
 */
public class ActorUtils<T extends Actor> implements Pool.Poolable {
    public static Pool<ActorUtils<Actor>> pool = new DefaultPool<>(ActorUtils::new);

    public static <T extends Actor> ActorUtils<T> wrap(T actor) {
        return (ActorUtils<T>)pool.obtain().set(actor);
    }

    private T actor;

    protected ActorUtils() {
    }

    @Override
    public void reset() {
        actor = null;
    }

    /** 拆除包装并回收包装器 */
    public T free() {
        var a = actor;
        pool.free((ActorUtils<Actor>)this);
        return a;
    }

    public ActorUtils<T> set(T actor) {
        this.actor = actor;
        return this;
    }

    public ActorUtils<T> update(Cons<T> updater) {
        ActorUtils.updater(actor, updater);
        return this;
    }

    public ActorUtils<T> visible(Boolf<T> visible) {
        ActorUtils.setVisible(actor, visible);
        return this;
    }

    public ActorUtils<T> click(Cons<T> clicked) {
        ActorUtils.clicked(actor, clicked);
        return this;
    }

    public ActorUtils<T> with(Cons<T> Cons) {
        Cons.get(actor);
        return this;
    }

    public ActorUtils<T> fill(Cons<Table> build) {
        if (actor instanceof Table t) {
            ActorUtils.fill(t, build);
        }
        return this;
    }

    public static <T extends Table> T fill(T actor, Cons<Table> build) {
        Table table = new Table();
        table.setFillParent(true);
        build.get(table);
        actor.addActor(table);

        return actor;
    }

    public static <T extends Actor> T updater(T actor, Cons<T> updater) {
        RunnableAction run = new RunnableAction();
        run.setRunnable(() -> updater.get(actor));
        RepeatAction repeat = new RepeatAction();
        repeat.setCount(RepeatAction.FOREVER);
        repeat.setAction(run);
        actor.addAction(repeat);

        return actor;
    }

    public static <T extends Actor> T setVisible(T actor, Boolf<T> visible) {
        RunnableAction run = new RunnableAction();
        run.setRunnable(() -> actor.setVisible(visible.get(actor)));
        RepeatAction repeat = new RepeatAction();
        repeat.setCount(RepeatAction.FOREVER);
        repeat.setAction(run);
        actor.addAction(repeat);

        return actor;
    }

    public static <T extends Actor> T clicked(T actor, Cons<T> clicked) {
        actor.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clicked.get(actor);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        return actor;
    }

    /**
     * 鼠标悬停在滚动区域时自动接管滚轮焦点，离开后释放。
     */
    public static <T extends ScrollPane> T bindHoverScrollFocus(T scrollPane) {
        final Vector2 pointer = new Vector2();
        final boolean[] hovering = {false};

        scrollPane.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                var stage = scrollPane.getStage();
                if (stage != null) {
                    stage.setScrollFocus(scrollPane);
                }
                hovering[0] = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (toActor != null && (toActor == scrollPane || toActor.isDescendantOf(scrollPane))) {
                    return;
                }

                var stage = scrollPane.getStage();
                if (stage != null && stage.getScrollFocus() == scrollPane) {
                    stage.setScrollFocus(null);
                }
                hovering[0] = false;
            }
        });

        updater(scrollPane, pane -> {
            var stage = pane.getStage();
            if (stage == null) {
                hovering[0] = false;
                return;
            }

            if (!hovering[0] && stage.getScrollFocus() != pane) {
                return;
            }

            if (!pane.isVisible()) {
                if (stage.getScrollFocus() == pane) {
                    stage.setScrollFocus(null);
                }
                hovering[0] = false;
                return;
            }

            pointer.set(Gdx.input.getX(), Gdx.input.getY());
            stage.screenToStageCoordinates(pointer);

            Actor hit = stage.hit(pointer.x, pointer.y, false);
            ScrollPane hoveredPane = findNearestScrollPane(hit);
            boolean inside = hoveredPane == pane;

            if (inside) {
                if (stage.getScrollFocus() != pane) {
                    stage.setScrollFocus(pane);
                }
                hovering[0] = true;
                return;
            }

            if (hovering[0]) {
                if (stage.getScrollFocus() == pane) {
                    stage.setScrollFocus(null);
                }
                hovering[0] = false;
            }
        });

        return scrollPane;
    }

    private static ScrollPane findNearestScrollPane(Actor actor) {
        Actor current = actor;
        while (current != null) {
            if (current instanceof ScrollPane pane) {
                return pane;
            }
            current = current.getParent();
        }
        return null;
    }

    /**
     * 查询元素是否存在符合条件的父母
     */
    public static boolean isDescendantOf(Actor self, Boolf<Actor> boolf) {
        Actor parent = self;
        do {
            if (boolf.get(parent)) return true;
            parent = parent.getParent();
        } while (parent != null);
        return false;
    }

    /**
     * 查询元素是否存在符合条件的子元素
     */
    public static boolean isAscendantOf(Actor self, Boolf<Actor> boolf) {
        Actor parent = self;
        do {
            if (boolf.get(parent)) return true;
            parent = parent.getParent();
        } while (parent != null);
        return false;
    }

    /**
     * 取得符合条件的父母. 总是返回第一个符合的元素.
     */
    public static Actor findDescendantOf(Actor self, Boolf<Actor> boolf) {
        Actor parent = self;
        do {
            if (boolf.get(parent)) return parent;
            parent = parent.getParent();
        } while (parent != null);
        return null;
    }

    /**
     * 取得符合条件的子元素. 总是返回第一个符合的元素.
     */
    public static Actor findAscendantOf(Actor self, Boolf<Actor> boolf) {
        Actor parent = self;
        do {
            if (boolf.get(parent)) return parent;
            parent = parent.getParent();
        } while (parent != null);
        return null;
    }

    public static TextField.TextFieldFilter digitOnly = new TextField.TextFieldFilter.DigitsOnlyFilter(), floatOnly = (textField, c) -> Character.isDigit(c) || (textField.getText().contains(".") && c == '.');
}
