package io.blackdeluxecat.painttd;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.enitiy.*;

import static io.blackdeluxecat.painttd.Vars.world;

public class Renderer{

    public static ShapeRenderer shaper = new ShapeRenderer();
    public void draw(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Core.batch.begin();
        Core.batch.draw(Core.atlas.findRegion("libgdx"), 0, 0);
        Core.batch.end();

        drawWorld();
        world.entity.forEach(Entity::draw);
    }

    public void drawWorld(){
        shaper.begin(ShapeRenderer.ShapeType.Filled);
        float scl = 32;//(double)Gdx.graphics.getWidth() / world.width;
        for(int x = 0; x < world.width; x++){
            for(int y = 0; y < world.height; y++){
                if(world.tiles.get(x + y * world.width) == 1){
                    shaper.setColor(Color.WHITE);
                    shaper.rect(x * scl, y * scl, scl, scl);
                }
            }
        }
        shaper.end();
    }
}
