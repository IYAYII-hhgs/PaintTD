package io.blackdeluxecat.painttd.systems.utils;

import com.artemis.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.systems.*;

@IsLogicProcess
public class TickTimer extends BaseSystem{
    @Override
    protected void processSystem(){
        Game.rules.ticks++;
    }
}
