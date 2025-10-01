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
    public void add(int sourceId, int targetId, DamageData data){
        queue.addLast(obtain().set(sourceId, targetId, data));
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
        public DamageData data;

        public DamageRequest(){
        }

        public DamageRequest set(int sourceId, int targetId, DamageData data){
            this.sourceId = sourceId;
            this.targetId = targetId;
            this.data = data;
            return this;
        }

        @Override
        public void reset(){
            super.reset();
            sourceId = targetId = -1;
            freeData(data);
        }
    }

    public static <T extends DamageData> T newData(Class<T> type){
        return Pools.obtain(type);
    }

    public static void freeData(DamageData data){
        Pools.free(data);
    }

    /**
     * 持有伤害的具体数据, 并作为伤害请求的类型标识符
     */
    public static abstract class DamageData implements Pool.Poolable{
    }

    public static class DirectDamageData extends DamageData{
        public float damage;

        public DirectDamageData(){
        }

        public DirectDamageData dmg(float damage){
            this.damage = damage;
            return this;
        }

        @Override
        public void reset(){
            damage = 0;
        }
    }

    public static class CollideDamageData extends DamageData{
        public CollideDamageData(){
        }

        @Override
        public void reset(){
        }
    }

    public static class SplashDamageData extends DamageData{
        public float x, y;
        public float damage;
        public float range;

        public SplashDamageData(){
        }

        public SplashDamageData pos(float x, float y){
            this.x = x;
            this.y = y;
            return this;
        }

        public SplashDamageData dmg(float damage, float range){
            this.damage = damage;
            this.range = range;
            return this;
        }

        @Override
        public void reset(){
            x = y = damage = range = 0;
        }
    }

    public static class DirectTileStainData extends DamageData{
        public int x, y;
        public float damage;

        public DirectTileStainData(){
        }

        public DirectTileStainData pos(int x, int y){
            this.x = x;
            this.y = y;
            return this;
        }

        public DirectTileStainData dmg(float damage){
            this.damage = damage;
            return this;
        }

        @Override
        public void reset(){
            x = y = 0;
            damage = 0;
        }
    }

    public static class SplashTileStainData extends DamageData{
        public int x, y;
        public float damage;
        public float range;

        public SplashTileStainData(){
        }

        public SplashTileStainData pos(int x, int y){
            this.x = x;
            this.y = y;
            return this;
        }

        public SplashTileStainData dmg(float damage, float range){
            this.damage = damage;
            this.range = range;
            return this;
        }

        @Override
        public void reset(){
            x = y = 0;
            damage = range = 0;
        }
    }
}
