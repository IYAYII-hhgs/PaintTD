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

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return screen.app.worldView.onScrolled(amountY, Gdx.input.getX(), Gdx.input.getY());
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.RIGHT) {
            return screen.app.worldView.onRightDragStart(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return screen.app.worldView.onRightDragMove(screenX, screenY);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.RIGHT) {
            screen.app.worldView.onRightDragEnd();
            return true;
        }
        return false;
    }
}
