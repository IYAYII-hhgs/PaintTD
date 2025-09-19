package io.blackdeluxecat.painttd.io;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.*;

/**
 * Json实现的首选项管理器.
 */
public class PrefMgr{
    public Json json = new Json();

    public FileHandle file = Core.gameDataFolder.child("prefs.json");

    public ObjectMap<String, Object> prefs;

    public boolean changed;

    public PrefMgr(){
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("str", String.class);
        json.addClassTag("i", Integer.class);
        json.addClassTag("f", Float.class);
        json.addClassTag("b", Boolean.class);
        json.addClassTag("l", Long.class);
    }

    public void clear(){
        prefs.clear();
        changed = true;
    }

    public void put(String key, Object value){
        prefs.put(key, value);
        changed = true;
    }

    public void putInt(String key, int value){
        put(key, value);
    }

    public void putFloat(String key, float value){
        put(key, value);
    }

    public void putBoolean(String key, boolean value){
        put(key, value);
    }

    public void putLong(String key, long value){
        put(key, value);
    }

    public void putDouble(String key, double value){
        put(key, value);
    }

    public @Null Object get(String key){
        return prefs.get(key);
    }

    public int getInt(String key, int def){
        Object v = get(key);
        if(v == null) return def;
        return (int)v;
    }

    public String getString(String key, String def){
        Object v = get(key);
        if(v == null) return def;
        return (String)v;
    }

    public boolean getBoolean(String key, boolean def){
        Object v = get(key);
        if(v == null) return def;
        return (boolean)v;
    }

    public float getFloat(String key, float def){
        Object v = get(key);
        if(v == null) return def;
        return (float)v;
    }

    public long getLong(String key, long def){
        Object v = get(key);
        if(v == null) return def;
        return (long)v;
    }

    public double getDouble(String key, double def){
        Object v = get(key);
        if(v == null) return def;
        return (double)v;
    }

    public void load(){
        try{
            if(file.exists()){
                prefs = json.fromJson(ObjectMap.class, file.readString());
            }else{
                prefs = new ObjectMap<>();
            }
        }catch(Exception e){
            Gdx.app.error("PrefMgr", "Failed to load prefs.", e);
            prefs = new ObjectMap<>();
        }
    }

    public void save(){
        if(changed){
            file.writeString(json.prettyPrint(json.toJson(prefs)), false);
            Gdx.app.debug("PrefMgr", prefs.size + " prefs saved.");
            changed = false;
        }
    }
}
