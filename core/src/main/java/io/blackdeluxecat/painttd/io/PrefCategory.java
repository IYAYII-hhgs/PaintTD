package io.blackdeluxecat.painttd.io;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.utils.func.*;

import static io.blackdeluxecat.painttd.Core.prefs;

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

    public static class PrefElem{
        public String name;
        public String description;

        public PrefElem(String name, String description){
            this.name = name;
            this.description = description;
        }
    }

    public static class BoolElem extends PrefElem{
        public boolean def;
        public BoolElem(String name, String description, boolean def){
            super(name, description);
            this.def = def;
            prefs.putBoolean(name, def);
        }
    }

    public interface PrefCategoryBuilder{
        void build(PrefCategory category, Table table);
    }

    public static abstract class PrefCateElemBuilder implements PrefCategoryBuilder{
        public ObjectMap<Class<? extends PrefElem>, Cons2<? extends PrefElem, Table>> builder;

        public <T extends PrefElem> void register(Class<T> clazz, Cons2<T, Table> cons){
            builder.put(clazz, cons);
        }
    }
}
