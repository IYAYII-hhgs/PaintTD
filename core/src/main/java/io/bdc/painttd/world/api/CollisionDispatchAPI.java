package io.bdc.painttd.world.api;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.lib.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

/**
 * 碰撞分发 API，位于检测层和应用层之间。
 * 检测 system 发现接触后直接调用这里的 emit 方法；
 * 应用层 system 则在这里注册各自的 handler。
 * <p>
 * 它的职责是把瞬时接触整理成稳定的 family 入口并转发出去。
 * 当前保留 EE（无向） 以及细分的 EC（单向） 入口；
 * 其中 EE 做了 pair 归一化和去重。
 */
public class CollisionDispatchAPI implements WorldAPI {
    public interface EeHandler {
        void handle(int eidA, int eidB);
    }

    public interface EcHandler {
        void handle(int eid, int cellIndex);
    }

    public final Array<EeHandler> undirectedEeHandlers = new Array<>();
    public final Array<EcHandler> ecWallHandlers = new Array<>();
    public final Array<EcHandler> ecStainHandlers = new Array<>();
    public final LongSet seenUndirectedPairs = new LongSet();

    public CollisionDebugStore debugStore;

    @Override
    public void onBind(WorldAccess binder) {
        debugStore = binder.getStore(CollisionDebugStore.class);
    }

    public void beginFrame() {
        seenUndirectedPairs.clear();
    }

    public void onEe(EeHandler handler) {
        undirectedEeHandlers.add(handler);
    }

    public void onEcWall(EcHandler handler) {
        ecWallHandlers.add(handler);
    }

    public void onEcStain(EcHandler handler) {
        ecStainHandlers.add(handler);
    }

    public void emitUndirectedEe(int eidA, int eidB) {
        if (eidA == eidB) {
            return;
        }

        int lo = Math.min(eidA, eidB);
        int hi = Math.max(eidA, eidB);
        long key = packPair(lo, hi);
        if (!seenUndirectedPairs.add(key)) {
            return;
        }

        debugStore.eeUniquePairCount += 1;
        for (int i = 0; i < undirectedEeHandlers.size; i++) {
            undirectedEeHandlers.get(i).handle(lo, hi);
        }
    }

    public void emitEcWall(int eid, int cellIndex) {
        for (int i = 0; i < ecWallHandlers.size; i++) {
            ecWallHandlers.get(i).handle(eid, cellIndex);
        }
    }

    public void emitEcStain(int eid, int cellIndex) {
        for (int i = 0; i < ecStainHandlers.size; i++) {
            ecStainHandlers.get(i).handle(eid, cellIndex);
        }
    }

    private long packPair(int lo, int hi) {
        return ((long) lo << 32) | (hi & 0xffffffffL);
    }
}
