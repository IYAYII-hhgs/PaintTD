package io.blackdeluxecat.painttd;

import com.badlogic.gdx.utils.*;

public class Time{
    public static long timer;
    static long lastTimer;

    public static void update(){
        lastTimer = timer;
        timer = TimeUtils.millis();
    }

    public static long getDelta(){
        return timer - lastTimer;
    }
}
