package io.bdc.painttd.world.store;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.lib.*;

import java.util.*;

/**
 * 按地图格缓存实体覆盖关系的空间索引。
 * <p>
 * 每个活动 node 表示一条 {@code eid -> cell} 关系，并同时挂在两条链上：
 * 一条是按 cell 组织的双向链，便于查询某格里有哪些实体；
 * 另一条是按 entity 组织的单向链，便于快速卸载某实体当前占据的所有格。
 */
public class TileBucketStore implements WorldStore, EntityOwner {
    private static final int NONE = -1;
    private static final int MIN_GROWTH = 64;

    public final int width;
    public final int height;
    public final IntHandlePool nodeHandles;

    public int[] headByCell;
    public int[] firstNodeByEid = new int[0];

    public int[] eidByNode = new int[0];
    public int[] cellByNode = new int[0];
    public int[] nextInCell = new int[0];
    public int[] prevInCell = new int[0];
    public int[] nextOfEntity = new int[0];

    public TileBucketStore(int width, int height) {
        this(width, height, 4096);
    }

    public TileBucketStore(int width, int height, int nodeGrowthStep) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("TileBucketStore size must be > 0: " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
        this.nodeHandles = new IntHandlePool(nodeGrowthStep);
        this.headByCell = new int[width * height];
        Arrays.fill(headByCell, NONE);
    }

    public int cellCount() {
        return headByCell.length;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int index(int x, int y) {
        return y * width + x;
    }

    public int headOfCell(int cell) {
        return headByCell[cell];
    }

    public int firstNodeOfEntity(int eid) {
        if (eid < 0 || eid >= firstNodeByEid.length) {
            return NONE;
        }
        return firstNodeByEid[eid];
    }

    public boolean hasCoverage(int eid) {
        return firstNodeOfEntity(eid) != NONE;
    }

    public int activeNodeCount() {
        return nodeHandles.allocatedCount();
    }

    public void clear() {
        Arrays.fill(headByCell, NONE);
        Arrays.fill(firstNodeByEid, NONE);
        Arrays.fill(eidByNode, NONE);
        Arrays.fill(cellByNode, NONE);
        Arrays.fill(nextInCell, NONE);
        Arrays.fill(prevInCell, NONE);
        Arrays.fill(nextOfEntity, NONE);
        nodeHandles.clear();
    }

    public void removeEntity(int eid) {
        if (eid < 0 || eid >= firstNodeByEid.length) {
            return;
        }

        int node = firstNodeByEid[eid];
        firstNodeByEid[eid] = NONE;
        while (node != NONE) {
            int nextNode = nextOfEntity[node];
            unlinkNodeFromCell(node);
            clearNode(node);
            if (!nodeHandles.free(node)) {
                throw new IllegalStateException("TileBucketStore failed to free node: " + node);
            }
            node = nextNode;
        }
    }

    public void reinsertSquare(int eid, float centerX, float centerY, float size) {
        removeEntity(eid);
        if (size <= 0f) {
            return;
        }

        float half = size * 0.5f;
        insertAabb(eid, centerX - half, centerY - half, centerX + half, centerY + half);
    }

    public void reinsertCircle(int eid, float centerX, float centerY, float radius) {
        removeEntity(eid);
        if (radius <= 0f) {
            return;
        }

        int minCellX = MathUtils.floor(centerX - radius);
        int maxCellX = MathUtils.floor(centerX + radius - MathUtils.FLOAT_ROUNDING_ERROR);
        int minCellY = MathUtils.floor(centerY - radius);
        int maxCellY = MathUtils.floor(centerY + radius - MathUtils.FLOAT_ROUNDING_ERROR);
        if (maxCellX < 0 || maxCellY < 0 || minCellX >= width || minCellY >= height) {
            return;
        }

        minCellX = MathUtils.clamp(minCellX, 0, width - 1);
        maxCellX = MathUtils.clamp(maxCellX, 0, width - 1);
        minCellY = MathUtils.clamp(minCellY, 0, height - 1);
        maxCellY = MathUtils.clamp(maxCellY, 0, height - 1);

        float radius2 = radius * radius;
        for (int y = minCellY; y <= maxCellY; y++) {
            for (int x = minCellX; x <= maxCellX; x++) {
                if (!circleIntersectsCell(centerX, centerY, radius2, x, y)) {
                    continue;
                }
                linkNode(eid, index(x, y));
            }
        }
    }

    private void insertAabb(int eid, float minX, float minY, float maxX, float maxY) {
        int minCellX = MathUtils.floor(minX);
        int maxCellX = MathUtils.floor(maxX - MathUtils.FLOAT_ROUNDING_ERROR);
        int minCellY = MathUtils.floor(minY);
        int maxCellY = MathUtils.floor(maxY - MathUtils.FLOAT_ROUNDING_ERROR);
        if (maxCellX < 0 || maxCellY < 0 || minCellX >= width || minCellY >= height) {
            return;
        }

        minCellX = MathUtils.clamp(minCellX, 0, width - 1);
        maxCellX = MathUtils.clamp(maxCellX, 0, width - 1);
        minCellY = MathUtils.clamp(minCellY, 0, height - 1);
        maxCellY = MathUtils.clamp(maxCellY, 0, height - 1);
        for (int y = minCellY; y <= maxCellY; y++) {
            for (int x = minCellX; x <= maxCellX; x++) {
                linkNode(eid, index(x, y));
            }
        }
    }

    private boolean circleIntersectsCell(float centerX, float centerY, float radius2, int cellX, int cellY) {
        float closestX = MathUtils.clamp(centerX, cellX, cellX + 1f);
        float closestY = MathUtils.clamp(centerY, cellY, cellY + 1f);
        float dx = centerX - closestX;
        float dy = centerY - closestY;
        return dx * dx + dy * dy <= radius2;
    }

    private void linkNode(int eid, int cell) {
        ensureEntityCapacity(eid + 1);

        int node = nodeHandles.alloc();
        ensureNodeCapacity(Math.max(node + 1, nodeHandles.capacity()));

        eidByNode[node] = eid;
        cellByNode[node] = cell;

        int oldCellHead = headByCell[cell];
        prevInCell[node] = NONE;
        nextInCell[node] = oldCellHead;
        if (oldCellHead != NONE) {
            prevInCell[oldCellHead] = node;
        }
        headByCell[cell] = node;

        nextOfEntity[node] = firstNodeByEid[eid];
        firstNodeByEid[eid] = node;
    }

    private void unlinkNodeFromCell(int node) {
        int cell = cellByNode[node];
        int prev = prevInCell[node];
        int next = nextInCell[node];

        if (prev != NONE) {
            nextInCell[prev] = next;
        } else if (cell != NONE) {
            headByCell[cell] = next;
        }

        if (next != NONE) {
            prevInCell[next] = prev;
        }
    }

    private void clearNode(int node) {
        eidByNode[node] = NONE;
        cellByNode[node] = NONE;
        nextInCell[node] = NONE;
        prevInCell[node] = NONE;
        nextOfEntity[node] = NONE;
    }

    private void ensureEntityCapacity(int minCapacity) {
        if (firstNodeByEid.length >= minCapacity) {
            return;
        }
        int oldLength = firstNodeByEid.length;
        int newCapacity = nextCapacity(oldLength, minCapacity);
        firstNodeByEid = Arrays.copyOf(firstNodeByEid, newCapacity);
        Arrays.fill(firstNodeByEid, oldLength, newCapacity, NONE);
    }

    private void ensureNodeCapacity(int minCapacity) {
        if (eidByNode.length >= minCapacity) {
            return;
        }

        int oldLength = eidByNode.length;
        int newCapacity = nextCapacity(oldLength, minCapacity);
        eidByNode = Arrays.copyOf(eidByNode, newCapacity);
        cellByNode = Arrays.copyOf(cellByNode, newCapacity);
        nextInCell = Arrays.copyOf(nextInCell, newCapacity);
        prevInCell = Arrays.copyOf(prevInCell, newCapacity);
        nextOfEntity = Arrays.copyOf(nextOfEntity, newCapacity);
        Arrays.fill(eidByNode, oldLength, newCapacity, NONE);
        Arrays.fill(cellByNode, oldLength, newCapacity, NONE);
        Arrays.fill(nextInCell, oldLength, newCapacity, NONE);
        Arrays.fill(prevInCell, oldLength, newCapacity, NONE);
        Arrays.fill(nextOfEntity, oldLength, newCapacity, NONE);
    }

    private int nextCapacity(int currentCapacity, int minCapacity) {
        int capacity = Math.max(currentCapacity, MIN_GROWTH);
        while (capacity < minCapacity) {
            capacity = Math.max(capacity + (capacity >> 1), capacity + MIN_GROWTH);
        }
        return capacity;
    }

    @Override
    public void onEntityDestroy(int entityId) {
        removeEntity(entityId);
    }
}
