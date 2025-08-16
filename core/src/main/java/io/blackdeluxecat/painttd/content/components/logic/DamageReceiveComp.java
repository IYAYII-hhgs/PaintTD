package io.blackdeluxecat.painttd.content.components.logic;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.*;

public class DamageReceiveComp extends CopyableComponent{
    public FloatArray damages = new FloatArray();

    public void add(float amount){
        damages.add(amount);
    }

    @Override
    public CopyableComponent copy(CopyableComponent other){
        //无需copy
        return this;
    }

    /**
     * 清空记录.
     */
    @Override
    protected void reset(){
        damages.clear();
        //是否需要resize释放内存?
    }
}
