package io.blackdeluxecat.painttd.utils.color;

import com.badlogic.gdx.graphics.Color;

public class HSVColor {
    float h, s, v;
    int index;

    public HSVColor(float h, float s, float v, int index) {
        this.h = h;
        this.s = s;
        this.v = v;
        this.index = index;
    }
    public HSVColor(float h, float s, float v) {
        this.h = h;
        this.s = s;
        this.v = v;
    }

    public HSVColor(Color color) {
        float[] hsv = new float[3];
        color.toHsv(hsv);
        this.h = hsv[0];
        this.s = hsv[1];
        this.v = hsv[2];
    }

    public Color toRGB() {
        Color c = new Color().fromHsv(h, s, v);
        c.a = 1.0f;
        return c;
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
