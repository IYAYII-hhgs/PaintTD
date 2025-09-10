package io.blackdeluxecat.painttd.systems;

import com.artemis.*;

import static io.blackdeluxecat.painttd.game.Game.*;

@IsLogicProcess
public class FlowFieldUpdate extends BaseSystem{
    @Override
    protected void processSystem(){
        flowField.rebuild();
    }
}
