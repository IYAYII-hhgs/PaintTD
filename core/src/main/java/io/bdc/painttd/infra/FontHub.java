package io.bdc.painttd.infra;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.*;

public class FontHub {
    public static final String DEF_FONT_KEY = "font-def.ttf";
    public static final String DEF_FONT_FILE = "fonts/SourceHanSans-VF.ttf";

    public AssetHub assets;
    public BitmapFont def;

    public FontHub(AssetHub assets) {
        this.assets = assets;
    }

    public void loadFonts() {
        FreeTypeFontLoaderParameter param = new FreeTypeFontLoaderParameter();
        param.fontFileName = DEF_FONT_FILE;
        param.fontParameters.size = 24;
        param.fontParameters.incremental = true;

        assets.getManager().load(DEF_FONT_KEY, BitmapFont.class, param);
        assets.getManager().finishLoading();
        def = assets.manager.get(DEF_FONT_KEY, BitmapFont.class);
    }
}
