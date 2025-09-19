package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;

public class SettingsDialog extends BaseDialog{
    public SettingsDialog(){
        super("选项");
        setBackground(Styles.black8);
        addCloseButton();
    }
}
