package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import static io.blackdeluxecat.painttd.ui.Fonts.standard;

public class Styles{
    public static float buttonSize = 32f;
    public static TextButton.TextButtonStyle sTextB;
    public static Label.LabelStyle sLabel;
    public static TextField.TextFieldStyle sTextF;

    public static void load(){
        sTextB = new TextButton.TextButtonStyle(null, null, null, standard);
        sLabel = new Label.LabelStyle(standard, Color.WHITE);
        sTextF = new TextField.TextFieldStyle(standard, Color.WHITE, null, null, null);
    }
}
