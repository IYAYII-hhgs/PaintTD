package io.blackdeluxecat.painttd.content.components.logic;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.*;
import io.blackdeluxecat.painttd.struct.*;

public class PathfindComp extends CopyableComponent{
    public Array<Vector2> currentPath;
    public int currentTargetIndex;
    public boolean isMoving = true;

    public PathfindComp(){
        currentPath = new Array<>();
    }

    @Override
    public PathfindComp copy(CopyableComponent other){
        var o = (PathfindComp)other;
        currentPath.clear();
        currentPath.addAll(o.currentPath);
        currentTargetIndex = o.currentTargetIndex;
        isMoving = o.isMoving;
        return this;
    }

    @Override
    protected void reset(){
        currentPath.clear();
        currentTargetIndex = 0;
        isMoving = false;
    }
}
