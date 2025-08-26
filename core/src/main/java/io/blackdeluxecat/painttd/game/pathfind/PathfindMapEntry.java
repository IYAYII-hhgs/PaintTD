package io.blackdeluxecat.painttd.game.pathfind;

import io.blackdeluxecat.painttd.game.*;

public interface PathfindMapEntry{
    default boolean isValid(int x, int y){
        var tile = Game.map.get(x, y);
        return tile != null;
    }

    default float cost(int x, int y){
        var tile = Game.map.get(x, y);
        return isValid(x, y) && !tile.isWall ? tile.layer : 1145141919810f;
    }
}
