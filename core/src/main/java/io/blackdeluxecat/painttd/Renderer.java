package io.blackdeluxecat.painttd;

import com.badlogic.gdx.graphics.glutils.*;

import static io.blackdeluxecat.painttd.Core.*;

public class Renderer{

    public void draw(){


        drawWorld();
    }

    public void drawWorld(){
        shaper.begin(ShapeRenderer.ShapeType.Filled);
        float scl = 32;//(double)Gdx.graphics.getWidth() / world.width;
        shaper.end();
    }
}
