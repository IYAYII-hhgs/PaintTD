package io.blackdeluxecat.painttd.game;

import com.artemis.*;
import com.artemis.utils.Sort;
import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.lib.func.*;
import io.blackdeluxecat.painttd.lib.struct.*;

/**
 * 使用层管理系统优先级的系统执行策略.
 * 注册的层和系统均不可删除.
 * 发生注册和修改后, 必须调用 {@link #sort()} 进行排序.
 * */
public class LayerInvocationStrategy extends InvocationStrategy{
    protected Array<Layer> layers = new Array<>();

    public LayerInvocationStrategy(){}

    @Override
    protected void initialize(){
        super.initialize();
        sort();
    }

    public Layer registerLayer(String name, float z){
        Layer l;
        if((l = getLayer(z)) != null){
            Gdx.app.log("LayerSystem", "Layer with z = " + z + " already exists.");
            return l;
        }
        if((l = getLayer(name)) != null){
            Gdx.app.log("LayerSystem", "Layer with name " + name + " already exists.");
            return l;
        }

        l = new Layer(name, z);
        layers.add(l);
        return l;
    }

    public @Null Layer getLayer(String name){
        return IterateUtils.first(layers, l -> l.name.equals(name));
    }

    public @Null Layer getLayer(float z){
        return IterateUtils.first(layers, l -> l.z == z);
    }

    public @Null Layer getLayer(BaseSystem system){
        return IterateUtils.first(layers, l -> l.systems.contains(system, true));
    }

    public float getZ(BaseSystem system){
        Layer l = getLayer(system);
        return l != null ? l.z : 0;
    }

    /**排序 {@link #systems}. 当修改了某个Layer之后, 进行排序. 三个Manager不参与排序.*/
    public void sort(){
        Sort.instance().sort(systems.getData(), (s1, s2) -> {
            float z1 = getZ(s1);
            float z2 = getZ(s2);
            return Float.compare(z1, z2);
        }, 2, systems.size());
    }

    public static class Layer implements Comparable<Layer>{
        public final String name;
        /**一旦修改, 必须排序LayerManager.*/
        protected float z;
        public Array<BaseSystem> systems = new Array<>();

        public Layer(String name, float z){
            this.name = name;
            this.z = z;
        }

        public boolean add(BaseSystem system){
            if(systems.contains(system, true)) return false;
            systems.add(system);
            return true;
        }

        public void with(Cons<Layer> cons){
            cons.get(this);
        }

        public float z(){
            return z;
        }

        public void z(float z){
            this.z = z;
        }

        public int count(){
            return systems.size;
        }

        @Override
        public int compareTo(Layer o){
            return Float.compare(z, o.z);
        }
    }
}
