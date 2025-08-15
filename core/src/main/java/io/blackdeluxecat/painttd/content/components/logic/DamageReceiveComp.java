package io.blackdeluxecat.painttd.content.components.logic;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.*;

public class DamageReceiveComp extends CopyableComponent{
    public FloatArray received = new FloatArray();

    public void add(float amount){
        received.add(amount);
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        //无需copy
        return this;
    }

    @Override
    protected void reset(){
        received.clear();
        //是否需要resize释放内存?
    }
}
