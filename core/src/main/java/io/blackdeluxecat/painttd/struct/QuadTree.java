package io.blackdeluxecat.painttd.struct;

import com.artemis.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.struct.func.*;

/**
 * TODO即时删除, 储存实体id的四叉树.
 * 点坐标为实体id映射的{@link io.blackdeluxecat.painttd.content.components.logic.PositionComp}
 *
 * @author BlackDeluxeCat
 * */
public class QuadTree{
    public ComponentMapper<PositionComp> pm;
    public final int maxValues;
    public final int maxDepth;

    public QuadTreeNode root;

    //public IntMap<QuadTreeNode> nodeMap = new IntMap<>();

    protected IntArray innerResults = new IntArray();
    protected Vector2 v1 = new Vector2();

    public QuadTree(int maxValue, int maxDepth){
        this.maxValues = maxValue;
        this.maxDepth = maxDepth;
    }

    public QuadTree(){
        this(60, 8);
    }

    public void create(World world, float x, float y, float width, float height){
        this.pm = world.getMapper(PositionComp.class);
        root = QuadTreeNode.pool.obtain();
        root.set(this, x, y, width, height, 0);
    }

    /**查询矩形*/
    //TODO 邻居节点查询优化
    public void queryRect(float x, float y, float width, float height, IntArray result, @Null IntBoolf filter){
        root.query(x, y, width, height, result);
        if(filter != null){
            for(int i = result.size - 1; i >= 0; i--)
                if(!filter.get(result.get(i))) result.removeIndex(i);
        }
    }

    public void eachRect(float x, float y, float w, float h, @Null IntBoolf filter, Intc cons){
        innerResults.clear();
        queryRect(x, y, w, h, innerResults, filter);
        if(innerResults.isEmpty()) return;
        for(int i = 0; i < innerResults.size; i++){
            cons.get(innerResults.get(i));
        }
    }

    public void eachCircle(float x, float y, float radius, @Null IntBoolf filter, Intc cons){
        eachRect(x - radius, y - radius, radius * 2, radius * 2, i -> {
            PositionComp pos = pm.get(i);
            return (pos != null && v1.set(pos.x , pos.y).sub(x, y).len2() < radius * radius) && (filter == null || filter.get(i));
        }, cons);
    }

    public static class QuadTreeNode implements Pool.Poolable{
        protected static final Pool<QuadTreeNode> pool = new Pool<>(128, 4096){
            protected QuadTreeNode newObject(){
                return new QuadTreeNode();
            }
        };

        protected static QuadTreeNode obtainChild(QuadTree tree, float x, float y, float width, float height, int depth) {
            QuadTreeNode child = pool.obtain();
            child.set(tree, x, y, width, height, depth);
            return child;
        }

        public QuadTree tree;
        public float width, height, x, y;
        public int depth;
        protected int count;

        public int[] values;
        public QuadTreeNode nw, ne, sw, se;

        public void set(QuadTree tree, float x, float y, float width, float height, int depth){
            this.tree = tree;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.depth = depth;
            this.count = 0;
            this.values = new int[tree.maxValues];
        }

        /**查询矩形*/
        public void query(float x, float y, float w, float h, IntArray result){
            if(x < this.x || x + w > this.x + this.width || y < this.y || y + h > this.y + this.height) return;

            if(count == -1){
                if(nw != null) nw.query(x, y, w, h, result);
                if(ne != null) ne.query(x, y, w, h, result);
                if(sw != null) sw.query(x, y, w, h, result);
                if(se != null) se.query(x, y, w, h, result);
            }

            if(count > 0){
                for(int i = 0; i < count; i++){
                    PositionComp pos = tree.pm.get(values[i]);
                    if(pos != null && pos.x >= x && pos.x <= x + w && pos.y >= y && pos.y <= y + h){
                        result.add(values[i]);
                    }
                }
            }
        }

        /**添加一个整数值, 无唯一性检查*/
        public void add(int value, float valueX, float valueY){
            if(count == -1){
                addToChild(value, valueX, valueY);
            }else if(count == tree.maxValues){
                if(depth < tree.maxDepth){
                    split();
                    addToChild(value, valueX, valueY);
                }else{
                    throw new RuntimeException("QuadTree is full");
                }
            }else
                values[count++] = value;
        }

        protected void split() {
            int[] values = this.values;
            for (int i = 0; i < tree.maxValues; i++) addToChild(values[i], values[i + 1], values[i + 2]);
            // values isn't nulled because the trees are pooled.
            count = -1;
        }

        private void addToChild(int value, float valueX, float valueY) {
            QuadTreeNode child;
            float halfWidth = width / 2, halfHeight = height / 2;
            if (valueX < x + halfWidth) {
                if (valueY < y + halfHeight)
                    child = sw != null ? sw : (sw = obtainChild(tree, x, y, halfWidth, halfHeight, depth + 1));
                else
                    child = nw != null ? nw : (nw = obtainChild(tree, x, y + halfHeight, halfWidth, halfHeight, depth + 1));
            } else {
                if (valueY < y + halfHeight)
                    child = se != null ? se : (se = obtainChild(tree, x + halfWidth, y, halfWidth, halfHeight, depth + 1));
                else
                    child = ne != null ? ne : (ne = obtainChild(tree, x + halfWidth, y + halfHeight, halfWidth, halfHeight, depth + 1));
            }
            child.add(value, valueX, valueY);
        }

        @Override
        public void reset(){
            if (count == -1) {
                if (nw != null) {
                    pool.free(nw);
                    nw = null;
                }
                if (sw != null) {
                    pool.free(sw);
                    sw = null;
                }
                if (ne != null) {
                    pool.free(ne);
                    ne = null;
                }
                if (se != null) {
                    pool.free(se);
                    se = null;
                }
            }
            count = 0;
            values = null;
            tree = null;
        }
    }
}
