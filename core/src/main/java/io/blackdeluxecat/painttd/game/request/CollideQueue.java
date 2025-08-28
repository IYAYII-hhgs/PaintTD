package io.blackdeluxecat.painttd.game.request;

import com.badlogic.gdx.utils.*;

public class CollideQueue extends RequestQueue<CollideQueue.CollideRequest>{
    LongMap<CollideRequest> added = new LongMap<>();

    public void add(int e1, int e2){
        int max = Math.max(e1, e2);
        int min = Math.min(e1, e2);
        long key = ((long)max << 32) | min;
        if(added.get(key) == null){
            add(pool.obtain().set(min, max));
        }
    }

    @Override
    public CollideRequest obtain(){
        return pool.obtain();
    }

    @Override
    public void free(CollideRequest r){
        pool.free(r);
    }

    public static final Pool<CollideRequest> pool = new Pool<>(){
        @Override
        protected CollideRequest newObject(){
            return new CollideRequest();
        }
    };

    public static class CollideRequest extends Request{
        /**总是较小值*/
        public int e1;
        /**总是较大值*/
        public int e2;

        public CollideRequest(){}

        public CollideRequest set(int e1, int e2){
            this.e1 = e1;
            this.e2 = e2;
            return this;
        }
    }
}
