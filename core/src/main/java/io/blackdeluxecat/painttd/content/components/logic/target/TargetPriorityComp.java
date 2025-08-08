package io.blackdeluxecat.painttd.content.components.logic.target;

import io.blackdeluxecat.painttd.content.components.logic.*;

public class TargetPriorityComp extends CopyableComponent{
    public static int CLOSEST = 0, HEALTH = 1, HEALTH_REGEN = 2, ARMOR = 3;
    public static final int SORT_DESCENDING = 0, SORT_ASCENDING = 1;

    public int priority = CLOSEST;
    public int sort = SORT_DESCENDING;

    public TargetPriorityComp(){}

    public TargetPriorityComp(int priority, int sortDescending){
        this.priority = priority;
        this.sort = sortDescending;
    }

    public TargetPriorityComp(int priority){
        this.priority = priority;
    }

    @Override
    protected void reset(){
        priority = CLOSEST;
        sort = SORT_DESCENDING;
    }

    @Override
    public TargetPriorityComp copy(CopyableComponent other){
        TargetPriorityComp tpc = (TargetPriorityComp)other;
        priority = tpc.priority;
        return tpc;
    }

}