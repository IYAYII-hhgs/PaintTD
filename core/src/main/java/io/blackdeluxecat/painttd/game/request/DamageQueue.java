package io.blackdeluxecat.painttd.game.request;

import com.badlogic.gdx.utils.*;

/**
 * 伤害请求是来源和目标确定之后进行伤害应用
 * 任意系统提交的伤害请求被排队, 由伤害处理系统遍历请求并处理
 * 伤害请求保存来源和目标实体, 以及一个伤害类型. 伤害处理系统以伤害类型作为其过滤器
 * <p>
 * 在当前帧末尾, 清理已处理请求
 * 在重置世界时, 清理所有请求
 */
public class DamageQueue extends RequestQueue<DamageQueue.DamageRequest>{
    public void add(int sourceId, int targetId, float amount, DamageRequestType type){
        queue.addLast(obtain().set(sourceId, targetId, amount, type));
    }

    @Override
    public DamageRequest obtain(){
        return pool.obtain();
    }

    @Override
    public void free(DamageRequest damageRequest){
        pool.free(damageRequest);
    }

    public static final Pool<DamageRequest> pool = new Pool<>(){
        @Override
        protected DamageRequest newObject(){
            return new DamageRequest();
        }
    };

    public static class DamageRequest extends RequestQueue.Request{
        public int sourceId;
        public int targetId;
        public float amount;
        public DamageRequestType type;

        public DamageRequest(){
        }

        public DamageRequest set(int sourceId, int targetId, float amount, DamageRequestType type){
            this.sourceId = sourceId;
            this.targetId = targetId;
            this.amount = amount;
            this.type = type;
            return this;
        }
    }


    /**
     * 伤害请求类型, 以字符为唯一标识符
     */
    public static final class DamageRequestType{
        private static final ObjectMap<String, DamageRequestType> types = new ObjectMap<>();

        private final String name;

        private DamageRequestType(String name){
            this.name = name;
        }

        public static DamageRequestType get(String name){
            if(types.containsKey(name)) return types.get(name);
            var type = new DamageRequestType(name);
            types.put(name, type);
            return type;
        }

        public String name(){
            return name;
        }

        public static final DamageRequestType collide = DamageRequestType.get("collide"),
            direct = DamageRequestType.get("direct");
    }

}
