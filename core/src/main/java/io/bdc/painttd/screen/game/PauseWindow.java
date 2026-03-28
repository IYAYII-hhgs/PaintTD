package io.bdc.painttd.screen.game;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class PauseWindow extends Window {
    public GameScreen screen;

    public PauseWindow(GameScreen screen) {
        super("Pause", screen.app.skins.skin);
        this.screen = screen;

        setName("pause-window");
        setModal(true);
        setMovable(false);
        setResizable(false);

        build();
    }

    private void build() {
        defaults().pad(8f).left();

        Label hint = new Label("Press Esc to resume or return to the main menu.", screen.app.skins.skin);
        hint.setWrap(true);

        TextButton resumeButton = new TextButton("Resume", screen.app.skins.skin);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.resumeFromPause();
            }
        });

        TextButton returnButton = new TextButton("Return to Main Menu", screen.app.skins.skin);
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.returnToMenu();
            }
        });

        Table buttons = new Table(screen.app.skins.skin);
        buttons.defaults().pad(4f).width(220f);
        buttons.add(resumeButton).fillX();
        buttons.row();
        buttons.add(returnButton).fillX();

        add(hint).width(260f);
        row();
        add(buttons).growX();
        pack();
    }
}
