package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.blackdeluxecat.painttd.*;

import static io.blackdeluxecat.painttd.ui.Fonts.*;

public class Styles{
    public static float buttonSize = 32f;
    public static TextureAtlas.AtlasRegion whiteRegion;

    public static TextButton.TextButtonStyle sTextB;
    public static Label.LabelStyle sLabel;
    public static TextField.TextFieldStyle sTextF;
    public static Window.WindowStyle sWindow;

    public static TextureRegionDrawable white;
    public static Drawable black, black3, black5, black8;

    public static void load(){
        whiteRegion = Core.atlas.findRegion("white");
        white = new TextureRegionDrawable(whiteRegion);
        black = white.tint(Color.BLACK);
        black3 = white.tint(new Color(0f, 0f, 0f, 0.3f));
        black5 = white.tint(new Color(0f, 0f, 0f, 0.5f));
        black8 = white.tint(new Color(0f, 0f, 0f, 0.8f));

        sTextB = new TextButton.TextButtonStyle(null, null, null, standard);
        sLabel = new Label.LabelStyle(standard, Color.WHITE);
        sTextF = new TextField.TextFieldStyle(standard, Color.WHITE, null, null, null);

        sWindow = new Window.WindowStyle(standard, new Color(0.1f, 0.2f, 1f, 1f), black5);
    }
}
