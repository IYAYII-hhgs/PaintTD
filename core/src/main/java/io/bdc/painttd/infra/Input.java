package io.bdc.painttd.infra;

import com.badlogic.gdx.*;

public class Input {
    public static InputMultiplexer inputMap = new InputMultiplexer();

    public static void load() {
        Gdx.input.setInputProcessor(inputMap);
        inputMap.addProcessor(UI.stage);
    }

    public static void dispose() {

    }
}
