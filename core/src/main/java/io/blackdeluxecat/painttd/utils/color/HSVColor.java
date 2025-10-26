package io.blackdeluxecat.painttd.utils.color;

import com.badlogic.gdx.graphics.Color;

public class HSVColor {
    float[] hsv = new float[3];
    float h, s, v;
    public HSVColor(float h, float s, float v) {
        this.h = h;
        this.s = s;
        this.v = v;
    }
    public HSVColor(int h, int s, int v) {
        this.h = h;
        this.s = s;
        this.v = v;
    }
    public HSVColor(Color color) {
        this.hsv = color.toHsv(hsv);
        this.h = hsv[0];
        this.s = hsv[1];
        this.v = hsv[2];
    }

    public Color toRGB() {
        return new Color().fromHsv(h, s, v);
    }

    @Override
    public String toString() {
        return "HSVColor{" +
            "h=" + h +
            ", s=" + s +
            ", v=" + v +
            '}';
    }
}
