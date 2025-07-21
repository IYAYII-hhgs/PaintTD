package io.blackdeluxecat.painttd;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.enitiy.*;

public class World{
    public int width, height;

    public IntArray tiles = new IntArray();

    public Array<Entity> entity = new Array<>();

    public World(int width, int height){
        resize(width, height);
    }

    public void act(){
        tiles.set(98, 1);
    }

    public void resize(int width, int height){
        this.width = width;
        this.height = height;
        tiles.setSize(width * height);
        for(int i = 0; i < tiles.size; i++){
            tiles.set(i, 0);
        }
    }
}
