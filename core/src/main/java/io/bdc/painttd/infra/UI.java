package io.bdc.painttd.infra;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.*;
import com.kotcrab.vis.ui.util.*;

public class UI {
    public static ScreenViewport viewport = new ScreenViewport();
    public static Stage stage = new Stage(viewport);

    public static ToastManager toasts;

    public static void load() {
        VisUI.load();

        toasts = new ToastManager(stage);
    }

    public static void dispose() {
        stage.dispose();
        VisUI.dispose();
    }
}
