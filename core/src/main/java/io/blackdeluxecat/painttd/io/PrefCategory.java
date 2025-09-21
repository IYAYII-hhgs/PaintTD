package io.blackdeluxecat.painttd.io;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.utils.func.*;

import static io.blackdeluxecat.painttd.Core.prefs;

/**
 * 选项类别. 有自己的ui文本和图标, 维护一组选项. 可自动为选项字段添加前缀.
 * */
public class PrefCategory{
    public static Array<PrefCategory> categories = new Array<>();

    public String name;
    public String prefix;
    public Drawable icon;

    public Array<PrefElem> elems = new Array<>();

    public PrefCategory(String name, @Null Drawable icon, String prefix){
        categories.add(this);
        this.prefix = prefix;
        this.name = name;
        this.icon = icon;
    }

    public PrefCategory(String name, @Null Drawable icon){
        this(name, icon, "");
    }

    public void add(PrefElem elem){
        elem.name = prefix + elem.name;
        elems.add(elem);
    }

    public PrefElem find(String name){
        String full = name + prefix;
        for(PrefElem elem : elems){
            if(elem.name.equals(full)){
                return elem;
            }
        }
        return null;
    }

    public void boolPref(String name, String description, boolean def, @Null Boolc changed){
        add(new BoolElem(name, description, def, changed));
    }

    public void intPref(String name, String description, int def, @Null Intc changed){
        add(new IntElem(name, description, def, changed));
    }

    public void floatPref(String name, String description, float def, @Null Floatc changed){
        add(new FloatElem(name, description, def, changed));
    }

    public void stringPref(String name, String description, String def, @Null Cons<String> changed){
        add(new StringElem(name, description, def, changed));
    }


    public static class PrefElem{
        public String name;
        public String description;

        public PrefElem(String name, String description){
            this.name = name;
            this.description = description;
        }

        public void updateValue(){
        }
    }

    public static class BoolElem extends PrefElem{
        public boolean value;
        @Null public Boolc changed;
        public BoolElem(String name, String description, boolean def, @Null Boolc changed){
            super(name, description);
            prefs.putBoolean(name, def);
            this.changed = changed;
        }

        @Override
        public void updateValue(){
            value = prefs.getBoolean(name);
        }
    }

    public static class IntElem extends PrefElem{
        public int value;
        @Null public Intc changed;
        public IntElem(String name, String description, int def, @Null Intc changed){
            super(name, description);
            prefs.putInt(name, def);
            this.changed = changed;
        }

        @Override
        public void updateValue(){
            value = prefs.getInt(name);
        }
    }

    public static class FloatElem extends PrefElem{
        public float value;
        @Null public Floatc changed;
        public FloatElem(String name, String description, float def, @Null Floatc changed){
            super(name, description);
            prefs.putFloat(name, def);
            this.changed = changed;
        }

        @Override
        public void updateValue(){
            value = prefs.getFloat(name);
        }
    }

    public static class StringElem extends PrefElem{
        public String value;
        @Null public Cons<String> changed;
        public StringElem(String name, String description, String def, @Null Cons<String> changed){
            super(name, description);
            prefs.put(name, def);
            this.changed = changed;
        }

        @Override
        public void updateValue(){
            value = prefs.getString(name);
        }
    }

    // Long and Double settings are useless, but I'll keep them for now
    public static class LongElem extends PrefElem{
        public long value;
        @Null public Cons<Long> changed;
        public LongElem(String name, String description, long def, @Null Cons<Long> changed){
            super(name, description);
            prefs.putLong(name, def);
            this.changed = changed;
        }

        @Override
        public void updateValue(){
            value = prefs.getLong(name);
        }
    }

    public static class DoubleElem extends PrefElem{
        public double value;
        @Null public Cons<Double> changed;
        public DoubleElem(String name, String description, double def, @Null Cons<Double> changed){
            super(name, description);
            prefs.putDouble(name, def);
            this.changed = changed;
        }

        @Override
        public void updateValue(){
            value = prefs.getDouble(name);
        }
    }

    public interface PrefCategoryBuilder{
        void build(PrefCategory category, Table table);
    }

    public static abstract class PrefCateElemBuilder implements PrefCategoryBuilder{
        public ObjectMap<Class<? extends PrefElem>, Cons2<PrefElem, Table>> builders = new ObjectMap<>();

        public void register(Class<? extends PrefElem> clazz, @Null Cons2<PrefElem, Table> cons){
            builders.put(clazz, cons);
        }
    }
}
