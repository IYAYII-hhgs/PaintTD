package io.blackdeluxecat.painttd.utils.color;

public class Test {
    public static void main(String[] args) {
        //System.out.println(Math.sin(Math.toRadians(30)));

        HSVColor blue = new HSVColor(240f, 1.0f, 1.0f);
        HSVColor green = new HSVColor(120f, 1.0f, 1.0f);
        HSVColor red = new HSVColor(0f, 0.5f, 1.0f);
        TileColor color = new TileColor();
        color.mixWith(red,green,blue);
        System.out.println(color);
    }
}
