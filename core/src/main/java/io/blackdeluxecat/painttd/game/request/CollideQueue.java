package io.blackdeluxecat.painttd.game.request;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.systems.request.*;

/**
 * 碰撞请求队列.
 *
 * <p>请求发生时机是两个实体进入对方碰撞箱时{@link CollideDetectQuadTree}
 * <p>请求移除时机是两个实体离开对方碰撞箱时
 * 期间请求应保持不变, 但可以标记handle, 一般不要使用{@link #clear()}和{@link #clearHandled()}, 交由系统自动管理!
 */
public class CollideQueue extends RequestQueue<CollideQueue.CollideRequest>{
    LongMap<CollideRequest> added = new LongMap<>();

    public void add(int source, int target){
        long key = ((long)source << 32) | target;
        if(added.get(key) == null){
            var req = pool.obtain().set(source, target);
            added.put(key, req);
            add(req);
        }
    }

    @Override
    public CollideRequest obtain(){
        return pool.obtain();
    }

    public boolean isColliding(int source, int target){
        long key = ((long)source << 32) | target;
        return added.get(key) != null;
    }

    @Override
    public void free(CollideRequest r){
        added.remove(r.getKey());
        pool.free(r);
    }

    public static final Pool<CollideRequest> pool = new Pool<>(){
        @Override
        protected CollideRequest newObject(){
            return new CollideRequest();
        }
    };

    public static class CollideRequest extends Request{
        public int source;
        public int target;

        public CollideRequest(){
        }

        public CollideRequest set(int source, int target){
            this.source = source;
            this.target = target;
            return this;
        }

        public long getKey(){
            return ((long)source << 32) | target;
        }
    }
}
