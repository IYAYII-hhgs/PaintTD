package io.blackdeluxecat.painttd.systems.render;

import com.artemis.*;
import com.artemis.annotations.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.*;

import static io.blackdeluxecat.painttd.Core.*;
import static io.blackdeluxecat.painttd.game.Game.flowField;

public class DrawTile extends BaseSystem{
    @Wire
    public ComponentMapper<TileComp> tileMapper;

    @Override
    protected void processSystem(){
        shaper.begin(ShapeRenderer.ShapeType.Line);
        for(int x = 0; x < Game.map.width; x++){
            for(int y = 0; y < Game.map.height; y++){
                int tile = Game.map.getTile(x, y);
                if(tile != -1 && tileMapper.get(tile).isWall){
                    shaper.setColor(Color.WHITE);
                    shaper.rect(x - 0.4f, y - 0.4f, 0.8f, 0.8f);
                }

                shaper.setColor(Color.DARK_GRAY);
                shaper.rect(x - 0.5f, y - 0.5f, 1, 1);

            }
        }
        shaper.end();

        shaper.begin(ShapeRenderer.ShapeType.Line);
        shaper.getColor().set(Color.GREEN, 0.4f);
        for(int x = 0; x < Game.map.width; x++){
            for(int y = 0; y < Game.map.height; y++){
                var v = flowField.getDirection(x, y, Vars.v1).scl(0.5f);
                shaper.triangle(x + v.x, y + v.y, x - v.scl(0.25f).y, y - v.x, x + v.y, y + v.x);
            }
        }
        shaper.end();
    }
}
