package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.game.*;

import static io.blackdeluxecat.painttd.Core.shaper;

public class DrawMapGrid extends BaseSystem{

    @Override
    protected void processSystem(){
        shaper.begin(ShapeRenderer.ShapeType.Line);
        for(int x = 0; x < Game.map.width; x++){
            for(int y = 0; y < Game.map.height; y++){
                if(Game.map.get(x, y).isWall){
                    shaper.setColor(Color.WHITE);
                    shaper.rect(x - 0.4f, y - 0.4f, 0.8f, 0.8f);
                }

                shaper.setColor(Color.DARK_GRAY);
                shaper.rect(x - 0.5f, y - 0.5f, 1, 1);
            }
        }
        shaper.end();
    }
}
