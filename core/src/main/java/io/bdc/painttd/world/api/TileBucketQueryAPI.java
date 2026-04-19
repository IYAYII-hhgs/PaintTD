package io.bdc.painttd.world.api;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.store.*;

import java.util.*;

/**
 * 基于 TileBucket 的去重查询 API。
 * <p>
 * 返回的 {@link #results} 为内部复用 scratch，调用方应在下一次查询前立即消费。
 */
public class TileBucketQueryAPI implements WorldAPI {
    private static final int MIN_GROWTH = 64;

    public TileBucketStore bucket;

    public final IntArray results = new IntArray();
    private int[] seenStampByEid = new int[0];
    private int queryStamp;

    @Override
    public void onBind(WorldAccess binder) {
        bucket = binder.getStore(TileBucketStore.class);
    }

    public IntArray collectCell(int cell) {
        beginQuery();
        if (cell < 0 || cell >= bucket.cellCount()) {
            return results;
        }
        collectCellInternal(cell);
        return results;
    }

    public IntArray collectCell(int x, int y) {
        beginQuery();
        if (!bucket.inBounds(x, y)) {
            return results;
        }
        collectCellInternal(bucket.index(x, y));
        return results;
    }

    public IntArray collectCellRect(int minCellX, int minCellY, int maxCellX, int maxCellY) {
        beginQuery();
        if (maxCellX < 0 || maxCellY < 0 || minCellX >= bucket.width || minCellY >= bucket.height || minCellX > maxCellX || minCellY > maxCellY) {
            return results;
        }

        minCellX = MathUtils.clamp(minCellX, 0, bucket.width - 1);
        maxCellX = MathUtils.clamp(maxCellX, 0, bucket.width - 1);
        minCellY = MathUtils.clamp(minCellY, 0, bucket.height - 1);
        maxCellY = MathUtils.clamp(maxCellY, 0, bucket.height - 1);
        if (minCellX > maxCellX || minCellY > maxCellY) {
            return results;
        }

        for (int y = minCellY; y <= maxCellY; y++) {
            for (int x = minCellX; x <= maxCellX; x++) {
                collectCellInternal(bucket.index(x, y));
            }
        }
        return results;
    }

    public IntArray collectAabb(float minX, float minY, float maxX, float maxY) {
        return collectCellRect(
                MathUtils.floor(minX),
                MathUtils.floor(minY),
                MathUtils.ceil(maxX - MathUtils.FLOAT_ROUNDING_ERROR),
                MathUtils.ceil(maxY - MathUtils.FLOAT_ROUNDING_ERROR)
        );
    }

    public IntArray collectRect(float x, float y, float w, float h) {
        return collectAabb(x, y, x + w, y + h);
    }

    public IntArray collectCRect(float cx, float cy, float w, float h) {
        return collectAabb(cx - w * 0.5f, cy - h * 0.5f, cx + w * 0.5f, cy + h * 0.5f);
    }

    public IntArray collectSquare(float centerX, float centerY, float size) {
        return collectCRect(centerX, centerY, size, size);
    }

    private void beginQuery() {
        if (queryStamp == Integer.MAX_VALUE) {
            Arrays.fill(seenStampByEid, 0);// 复审注: 如果stamp存放的是每次查询自增的queryStamp, 重置过程也许不需要fill 0.
            queryStamp = 1;
        } else {
            queryStamp += 1;
            if (queryStamp == 0) {
                queryStamp = 1;
            }
        }
        results.clear();
    }

    private void collectCellInternal(int cell) {
        for (int node = bucket.headByCell[cell]; node != -1; node = bucket.nextInCell[node]) {
            int eid = bucket.eidByNode[node];
            accept(eid);
        }
    }

    private void accept(int eid) {
        ensureSeenCapacity(eid + 1);
        if (seenStampByEid[eid] == queryStamp) {
            return;
        }
        seenStampByEid[eid] = queryStamp;
        results.add(eid);
    }

    private void ensureSeenCapacity(int minCapacity) {
        if (seenStampByEid.length >= minCapacity) {
            return;
        }
        int capacity = Math.max(seenStampByEid.length, MIN_GROWTH);
        while (capacity < minCapacity) {
            capacity = Math.max(capacity + (capacity >> 1), capacity + MIN_GROWTH);
        }
        seenStampByEid = Arrays.copyOf(seenStampByEid, capacity);
    }

    // ---- 调试入口 ----
    public int getQueryStamp() {
        return queryStamp;
    }
}
