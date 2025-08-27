package io.blackdeluxecat.painttd.systems;

import com.artemis.*;
import com.artemis.utils.*;
import io.blackdeluxecat.painttd.content.components.marker.*;

import static io.blackdeluxecat.painttd.game.Game.flowField;

public class FlowFieldUpdate extends BaseSystem{
    public boolean shouldRebuild;

    protected EntitySubscription coreStainSubscription;
    protected EntitySubscription.SubscriptionListener coreStainListener = new EntitySubscription.SubscriptionListener(){
        @Override
        public void inserted(IntBag entities){
            shouldRebuild = true;
        }

        @Override
        public void removed(IntBag entities){
            shouldRebuild = true;
        }
    };

    @Override
    protected void setWorld(World world){
        super.setWorld(world);
        coreStainSubscription = world.getAspectSubscriptionManager().get(Aspect.all(MarkerComp.CoreStain.class));
        coreStainSubscription.addSubscriptionListener(coreStainListener);
    }

    @Override
    protected void processSystem() {
        if(shouldRebuild){
            flowField.rebuild();
            shouldRebuild = false;
        }else{
            flowField.update();
        }
    }
}
