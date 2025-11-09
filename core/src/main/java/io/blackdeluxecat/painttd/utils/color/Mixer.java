package io.blackdeluxecat.painttd.utils.color;

import java.util.LinkedList;

@FunctionalInterface
public interface Mixer {
    default void mix(LinkedList<HSVColor> colors){
        for(HSVColor color : colors){
            mixWith(color);
        }
    }

    void mixWith(HSVColor color);

}
