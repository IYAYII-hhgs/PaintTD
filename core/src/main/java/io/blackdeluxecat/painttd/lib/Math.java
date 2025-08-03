package io.blackdeluxecat.painttd.lib;

public class Math{
    public static float mod(float a, float b){
        return a - b * (int)(a / b);
    }
}
