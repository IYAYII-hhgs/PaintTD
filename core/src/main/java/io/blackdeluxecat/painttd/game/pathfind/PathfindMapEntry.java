package io.blackdeluxecat.painttd.game.pathfind;

import io.blackdeluxecat.painttd.game.*;

public interface PathfindMapEntry{
    default boolean isValid(int x, int y){
        var tile = Game.map.get(x, y);
        return tile != null;
    }

    default float cost(int x, int y){
        int e = Game.map.getTileStain(x, y);
        if(e != -1 && Game.utils.tileStainMapper.get(e).isCore && Game.utils.healthMapper.get(e).health > 0){
            return 0;
        }
        var tile = Game.map.get(x, y);
        return isValid(x, y) && !tile.isWall ? tile.layer : 114514f;
    }
}
