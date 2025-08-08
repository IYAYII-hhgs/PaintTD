package io.blackdeluxecat.painttd.game;

import com.artemis.*;
import com.artemis.utils.*;
import io.blackdeluxecat.painttd.struct.*;

/**
 * 使用层管理系统优先级的系统执行策略.
 * 注册的层和系统均不可删除.
 * 发生注册和修改后, 必须调用 {@link #sort()} 进行排序.
 * */
public class LayerInvocationStrategy extends InvocationStrategy{
    public LayerManager<BaseSystem> lm = new LayerManager<>();

    public LayerInvocationStrategy(){}

    @Override
    protected void initialize(){
        super.initialize();
        sort();
    }

    /**排序 {@link #systems}. 当修改了某个Layer之后, 进行排序. 三个Manager不参与排序.*/
    public void sort(){
        lm.sort();
        Sort.instance().sort(systems.getData(), (s1, s2) -> {
            float z1 = lm.getZ(s1);
            float z2 = lm.getZ(s2);
            return Float.compare(z1, z2);
        }, 2, systems.size());
    }
}
