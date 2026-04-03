package io.bdc.painttd.infra;

import com.badlogic.gdx.graphics.g2d.*;
import io.bdc.painttd.*;
import io.bdc.painttd.lib.*;

public class RenderHub {
    public static Batch batch = new SpriteBatch();

    public static Fill fill = new Fill();
    public static Line line = new Line();
    public static TextureRegion white;

    public static float scl = 10f;

    public static void load() {
        white = PaintTD.app.assets.atlas.findRegion("white");
        fill.setBatch(batch);
        line.setBatch(batch);
        fill.setRegion(white);
        line.setRegion(white);
    }

    public static void dispose() {
        batch.dispose();
    }
}
