package io.bdc.painttd.content.def;

public class WorldDef {
    public final int id;

    public String stringId;

    public WorldDef(int id) {
        this.id = id;
        this.stringId = Integer.toString(id);
    }
}
