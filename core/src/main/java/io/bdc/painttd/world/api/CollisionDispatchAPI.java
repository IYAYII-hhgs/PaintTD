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
 * 它的职责不是做玩法结算，而是把瞬时接触按 family 整理后分发出去。
 * 当前保留三类入口：DirectedEE、UndirectedEE、EC。
 * 其中 UndirectedEE 在这里统一做 pair 归一化和去重.
 */
public class CollisionDispatchAPI implements WorldAPI {
    public interface EeHandler {
        void handle(int eidA, int eidB);
    }

    public interface EcHandler {
        void handle(int eid, int cellIndex);
    }

    public final Array<EeHandler> directedEeHandlers = new Array<>();
    public final Array<EeHandler> undirectedEeHandlers = new Array<>();
    public final Array<EcHandler> ecHandlers = new Array<>();
    public final LongSet seenUndirectedPairs = new LongSet();

    public CollisionDebugStore debugStore;

    @Override
    public void onBind(WorldAccess binder) {
        debugStore = binder.getStore(CollisionDebugStore.class);
    }

    public void beginFrame() {
        seenUndirectedPairs.clear();
    }

    public void onDirectedEe(EeHandler handler) {
        directedEeHandlers.add(handler);
    }

    public void onUndirectedEe(EeHandler handler) {
        undirectedEeHandlers.add(handler);
    }

    public void onEc(EcHandler handler) {
        ecHandlers.add(handler);
    }

    public void emitDirectedEe(int eidA, int eidB) {
        for (int i = 0; i < directedEeHandlers.size; i++) {
            directedEeHandlers.get(i).handle(eidA, eidB);
        }
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

    public void emitEc(int eid, int cellIndex) {
        for (int i = 0; i < ecHandlers.size; i++) {
            ecHandlers.get(i).handle(eid, cellIndex);
        }
    }

    private long packPair(int lo, int hi) {
        return ((long) lo << 32) | (hi & 0xffffffffL);
    }
}
