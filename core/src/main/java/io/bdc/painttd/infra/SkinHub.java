package io.bdc.painttd.infra;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.kotcrab.vis.ui.*;
import com.kotcrab.vis.ui.widget.*;

public class SkinHub {
    public FontHub fonts;
    public Skin skin;

    public SkinHub(FontHub fonts) {
        this.fonts = fonts;
    }

    public void createVisUiSkin() {
        VisUI.load();
        skin = VisUI.getSkin();
    }

    public void patchAllFonts() {
        replaceFontResources();
        replaceLabelStyles();
        replaceTextButtonStyles();
        replaceImageTextButtonStyles();
        replaceCheckBoxStyles();
        replaceTextFieldStyles();
        replaceListStyles();
        replaceSelectBoxStyles();
        replaceWindowStyles();
        replaceVisTextButtonStyles();
        replaceVisImageTextButtonStyles();
        replaceVisCheckBoxStyles();
        replaceVisTextFieldStyles();
        replaceLinkLabelStyles();
        replaceMenuItemStyles();
    }

    public void replaceFontResources() {
        ObjectMap<String, BitmapFont> fontResources = skin.getAll(BitmapFont.class);
        if (fontResources == null) {
            return;
        }

        for (ObjectMap.Entry<String, BitmapFont> entry : fontResources.entries()) {
            fontResources.put(entry.key, fonts.def);
        }
    }

    public void replaceLabelStyles() {
        ObjectMap<String, Label.LabelStyle> styles = skin.getAll(Label.LabelStyle.class);
        if (styles == null) return;
        for (Label.LabelStyle style : styles.values()) {
            style.font = fonts.def;
        }
    }

    public void replaceTextButtonStyles() {
        ObjectMap<String, TextButton.TextButtonStyle> styles = skin.getAll(TextButton.TextButtonStyle.class);
        if (styles == null) return;
        for (TextButton.TextButtonStyle style : styles.values()) {
            style.font = fonts.def;
        }
    }

    public void replaceImageTextButtonStyles() {
        ObjectMap<String, ImageTextButton.ImageTextButtonStyle> styles = skin.getAll(ImageTextButton.ImageTextButtonStyle.class);
        if (styles == null) return;
        for (ImageTextButton.ImageTextButtonStyle style : styles.values()) {
            style.font = fonts.def;
        }
    }

    public void replaceCheckBoxStyles() {
        ObjectMap<String, CheckBox.CheckBoxStyle> styles = skin.getAll(CheckBox.CheckBoxStyle.class);
        if (styles == null) return;
        for (CheckBox.CheckBoxStyle style : styles.values()) {
            style.font = fonts.def;
        }
    }

    public void replaceTextFieldStyles() {
        ObjectMap<String, TextField.TextFieldStyle> styles = skin.getAll(TextField.TextFieldStyle.class);
        if (styles == null) return;
        for (TextField.TextFieldStyle style : styles.values()) {
            style.font = fonts.def;
            if (style.messageFont != null) {
                style.messageFont = fonts.def;
            }
        }
    }

    public void replaceListStyles() {
        ObjectMap<String, List.ListStyle> styles = skin.getAll(List.ListStyle.class);
        if (styles == null) return;
        for (List.ListStyle style : styles.values()) {
            style.font = fonts.def;
        }
    }

    public void replaceSelectBoxStyles() {
        ObjectMap<String, SelectBox.SelectBoxStyle> styles = skin.getAll(SelectBox.SelectBoxStyle.class);
        if (styles == null) return;
        for (SelectBox.SelectBoxStyle style : styles.values()) {
            style.font = fonts.def;
            if (style.listStyle != null) {
                style.listStyle.font = fonts.def;
            }
        }
    }

    public void replaceWindowStyles() {
        ObjectMap<String, Window.WindowStyle> styles = skin.getAll(Window.WindowStyle.class);
        if (styles == null) return;
        for (Window.WindowStyle style : styles.values()) {
            style.titleFont = fonts.def;
        }
    }

    public void replaceVisTextButtonStyles() {
        ObjectMap<String, VisTextButton.VisTextButtonStyle> styles = skin.getAll(VisTextButton.VisTextButtonStyle.class);
        if (styles == null) return;
        for (VisTextButton.VisTextButtonStyle style : styles.values()) {
            style.font = fonts.def;
        }
    }

    public void replaceVisImageTextButtonStyles() {
        ObjectMap<String, VisImageTextButton.VisImageTextButtonStyle> styles = skin.getAll(VisImageTextButton.VisImageTextButtonStyle.class);
        if (styles == null) return;
        for (VisImageTextButton.VisImageTextButtonStyle style : styles.values()) {
            style.font = fonts.def;
        }
    }

    public void replaceVisCheckBoxStyles() {
        ObjectMap<String, VisCheckBox.VisCheckBoxStyle> styles = skin.getAll(VisCheckBox.VisCheckBoxStyle.class);
        if (styles == null) return;
        for (VisCheckBox.VisCheckBoxStyle style : styles.values()) {
            style.font = fonts.def;
        }
    }

    public void replaceVisTextFieldStyles() {
        ObjectMap<String, VisTextField.VisTextFieldStyle> styles = skin.getAll(VisTextField.VisTextFieldStyle.class);
        if (styles == null) return;
        for (VisTextField.VisTextFieldStyle style : styles.values()) {
            style.font = fonts.def;
            if (style.messageFont != null) {
                style.messageFont = fonts.def;
            }
        }
    }

    public void replaceLinkLabelStyles() {
        ObjectMap<String, LinkLabel.LinkLabelStyle> styles = skin.getAll(LinkLabel.LinkLabelStyle.class);
        if (styles == null) return;
        for (LinkLabel.LinkLabelStyle style : styles.values()) {
            style.font = fonts.def;
        }
    }

    public void replaceMenuItemStyles() {
        ObjectMap<String, MenuItem.MenuItemStyle> styles = skin.getAll(MenuItem.MenuItemStyle.class);
        if (styles == null) return;
        for (MenuItem.MenuItemStyle style : styles.values()) {
            style.font = fonts.def;
        }
    }

    public void dispose() {
        // AssetManager owns the shared font resource for this bootstrap step.
        skin = null;
        //VisUI.dispose();
    }
}
