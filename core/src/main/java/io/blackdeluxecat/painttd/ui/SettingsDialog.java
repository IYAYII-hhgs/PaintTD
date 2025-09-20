package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.utils.func.*;

public class SettingsDialog extends BaseDialog{

    public SettingsDialog(){
        super("选项");
        setBackground(Styles.black8);
        addCloseButton();
    }

    public abstract static class Setting{
        public String name;
        public String description;
        public Object value;
        public Object def;

        public Setting(String name, String description, Object def){
            this.name = name;
            this.description = description;
            this.def = def;
            setDef();
        }

        public Object getValue(){
            return value;
        }

        public void setDef(){
            Core.prefs.put(name, def);
        }

        /**为该选项创建设置控件*/
        public abstract void build(Table table);
    }

    public static class BoolSetting extends Setting{
        public Boolc changed;
        public BoolSetting(String name, String description, boolean b, Boolc changed){
            super(name, description, b);
        }

        @Override
        public void setDef(){

        }

        @Override
        public void build(Table table){

        }
    }
}
