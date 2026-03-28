package io.bdc.painttd.screen.game;

import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.*;

public class GameScreenInputAdapter extends InputAdapter {
    public GameScreen screen;

    public GameScreenInputAdapter(GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode != Keys.ESCAPE) {
            return false;
        }

        return screen.togglePauseWindow();
    }
}
