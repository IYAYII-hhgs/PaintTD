package io.blackdeluxecat.painttd.content.components.event;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.*;

public class DamageDealEventComp extends EventComp{
    public FloatArray damages = new FloatArray();

    public void add(float amount){
        damages.add(amount);
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
