package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.blackdeluxecat.painttd.game.*;

import static io.blackdeluxecat.painttd.Core.shaper;

public class MapGridDraw extends BaseSystem{

    @Override
    protected void processSystem(){
        shaper.begin(ShapeRenderer.ShapeType.Line);
        shaper.setColor(Color.DARK_GRAY);
        for(int x = 0; x < Game.map.width; x++){
            for(int y = 0; y < Game.map.height; y++){
                shaper.rect(x - 0.5f, y - 0.5f, 1, 1);
            }
        }
        shaper.end();
    }
}
