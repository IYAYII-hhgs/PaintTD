package io.bdc.painttd.content.def;

import com.badlogic.gdx.utils.*;

public class EntityDef {
    /** 持久化形态 */
    public String kind;
    public String variant = "";

    public final Array<String> tags = new Array<>();
    public final OrderedMap<String, Integer> intParams = new OrderedMap<>();
    public final OrderedMap<String, Float> floatParams = new OrderedMap<>();
    public final OrderedMap<String, String> stringParams = new OrderedMap<>();

    /** 注册形态 */
    public final int id;

    public int sizeX = 1;
    public int sizeY = 1;


    public EntityDef(int id) {
        this.id = id;
    }

    public EntityDef tag(String value) {
        tags.add(value);
        return this;
    }

    public EntityDef intParam(String key, int value) {
        intParams.put(key, value);
        return this;
    }

    public EntityDef floatParam(String key, float value) {
        floatParams.put(key, value);
        return this;
    }

    public EntityDef stringParam(String key, String value) {
        stringParams.put(key, value);
        return this;
    }

    public int intParamOr(String key, int fallback) {
        return intParams.get(key, fallback);
    }

    public float floatParamOr(String key, float fallback) {
        return floatParams.get(key, fallback);
    }

    public String stringParamOr(String key, String fallback) {
        return stringParams.get(key, fallback);
    }
}
