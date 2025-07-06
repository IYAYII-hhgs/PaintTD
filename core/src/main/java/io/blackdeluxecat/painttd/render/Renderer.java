package io.blackdeluxecat.painttd.render;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.core.*;

import static io.blackdeluxecat.painttd.Vars.world;

public class Renderer{

    public static ShapeRenderer shaper = new ShapeRenderer();
    public void draw(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Core.batch.begin();
        Core.batch.draw(Core.atlas.findRegion("libgdx"), 140, 210);
        Core.batch.end();

        shaper.begin(ShapeRenderer.ShapeType.Filled);
        shaper.setColor(Color.RED);
        shaper.rect(20f, 30f, 100f, 200f);
        drawWorld();
        shaper.end();
    }

    public void drawWorld(){
        for(int x = 0; x < world.width; x++){
            for(int y = 0; y < world.height; y++){
                if(world.tiles.get(x + y * world.width) == 1){
                    shaper.setColor(Color.WHITE);
                    shaper.rect(x * 5, y * 5, 5, 5);
                }
            }
        }
    }
}
