package io.blackdeluxecat.painttd.game.request;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.systems.request.*;

/**
 * 碰撞请求队列.
 *
 * <p>碰撞请求的创建时机是两个实体进入对方碰撞箱时(参见{@link CollideDetect})
 * <p>移除时机是两个实体离开对方碰撞箱时(参见{@link CollideQueueRemoveNoLongerOverlaps}).
 * 期间请求应保持不变, 但可以标记handle, 一般不要使用{@link #clear()}和{@link #clearHandled()}, 交由系统自动管理!
 * */
public class CollideQueue extends RequestQueue<CollideQueue.CollideRequest>{
    LongMap<CollideRequest> added = new LongMap<>();

    public void add(int e1, int e2){
        int max = Math.max(e1, e2);
        int min = Math.min(e1, e2);
        long key = ((long)min << 32) | max;
        if(added.get(key) == null){
            var req = pool.obtain().set(min, max);
            added.put(key, req);
            add(req);
        }
    }

    @Override
    public CollideRequest obtain(){
        return pool.obtain();
    }

    public boolean isColliding(int e1, int e2){
        int max = Math.max(e1, e2);
        int min = Math.min(e1, e2);
        long key = ((long)min << 32) | max;
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
        /**总是较小值, 不要直接修改!*/
        public int e1;
        /**总是较大值, 不要直接修改!*/
        public int e2;

        public CollideRequest(){}

        public CollideRequest set(int e1, int e2){
            this.e1 = e1;
            this.e2 = e2;
            return this;
        }

        public long getKey(){
            return ((long)e1 << 32) | e2;
        }
    }
}
