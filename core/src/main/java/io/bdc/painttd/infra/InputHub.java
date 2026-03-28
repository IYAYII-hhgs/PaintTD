package io.bdc.painttd.infra;

import com.badlogic.gdx.*;

public class InputHub {
    public InputMultiplexer multiplexer;

    public InputProcessor screenInput;

    public void create() {
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void addCoreInput(InputProcessor processor) {
        multiplexer.addProcessor(processor);
    }

    public void setScreenInput(InputProcessor nextInput) {
        if (screenInput != null) multiplexer.removeProcessor(screenInput);
        screenInput = nextInput;
        if (screenInput != null) multiplexer.addProcessor(screenInput);
    }

    public void dispose() {
        if (multiplexer != null && Gdx.input.getInputProcessor() == multiplexer) {
            Gdx.input.setInputProcessor(null);
        }
    }
}
