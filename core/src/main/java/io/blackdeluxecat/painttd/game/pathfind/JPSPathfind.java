package io.blackdeluxecat.painttd.game.pathfind;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.struct.*;

import java.util.*;

import static io.blackdeluxecat.painttd.Core.shaper;

public class JPSPathfind{

    public PathfindMapEntry entry = new PathfindMapEntry(){};

    // 主寻路函数
    public Array<Vector2> findPath(float startX, float startY, float targetX, float targetY){
        // 1. 转换为网格坐标
        int startGridX = (int)startX;
        int startGridY = (int)startY;
        int goalGridX = (int)targetX;
        int goalGridY = (int)targetY;

        // 2. 初始化
        PriorityQueue<Node> openList = new PriorityQueue<>(comparator);
        Array<Node> closedList = new Array<>();
        Node start = pool.obtain().set(startGridX, startGridY);
        Array<Vector2> result = null;

        // 3. 添加起点
        openList.add(start);

        // 4. 主循环
        while(!openList.isEmpty()){
            Node current = openList.poll();

            shaper.begin(ShapeRenderer.ShapeType.Filled);
            shaper.setColor(Vars.c1.set(Color.CYAN, 0.3f));
            shaper.rect(current.x - 0.2f, current.y - 0.2f, 0.4f, 0.4f);
            shaper.end();

            // 找到目标, 构建完整路径
            if(current.x == goalGridX && current.y == goalGridY){
                result = reconstructPath(current);
                break;
            }

            Array<Node> neighbors = findNeighbor(current, pool.obtain().set(goalGridX, goalGridY));

            for(Node neighbor : neighbors){
                if(closedList.contains(neighbor, false)){
                    continue;
                }

                float newG = current.g + distance(current, neighbor);
                if(!openList.contains(neighbor) || newG < neighbor.g){
                    neighbor.g = newG;
                    neighbor.h = heuristic(neighbor, goalGridX, goalGridY);
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = current;

                    if(!openList.contains(neighbor)){
                        openList.add(neighbor);
                    }
                }
            }

            closedList.add(current);
        }

        IterateUtils.each(closedList, node -> pool.free(node));
        while(!openList.isEmpty()){
            pool.free(openList.poll());
        }

        return result;
    }

    /**
     * 获取感兴趣的邻居
     */
    private Array<Node> findNeighbor(Node node, Node goal){
        Array<Node> successors = new Array<>();
        int x = node.x;
        int y = node.y;

        // 如果是起点，进行全方向探索
        if(node.parent == null){
            for(int dx = -1; dx <= 1; dx++){
                for(int dy = -1; dy <= 1; dy++){
                    if(dx == 0 && dy == 0) continue;
                    if(entry.isValid(x + dx, y + dy)) successors.add(pool.obtain().set(node.x + dx, node.y + dy));
                }
            }
            return successors;
        }

        // 获取移动方向
        int dx = Integer.compare(x, node.parent.x);
        int dy = Integer.compare(y, node.parent.y);

        //水平垂直方向, 对一个邻居和两个强迫邻居感兴趣
        if(dx != 0 && dy == 0){
            if(entry.isValid(x + dx, y)) successors.add(pool.obtain().set(x + dx, y));
            // 检查水平方向的强迫邻居
            if(!entry.isValid(x, y - 1) && entry.isValid(x + dx, y - 1)) successors.add(pool.obtain().set(x + dx, y - 1));
            if(!entry.isValid(x, y + 1) && entry.isValid(x + dx, y + 1)){
                successors.add(pool.obtain().set(x + dx, y + 1));
            }
        }
        else if(dx == 0 && dy != 0){
            if(entry.isValid(x, y + dy)) successors.add(pool.obtain().set(x, y + dy));
            // 检查垂直方向的强迫邻居
            if(!entry.isValid(x - 1, y) && entry.isValid(x - 1, y + dy)) successors.add(pool.obtain().set(x - 1, y + dy));
            if(!entry.isValid(x + 1, y) && entry.isValid(x + 1, y + dy)) successors.add(pool.obtain().set(x + 1, y + dy));
        }
        //对角线方向, 对三个邻居和两个强迫邻居感兴趣
        else{
            if(entry.isValid(x + dx, y)) successors.add(pool.obtain().set(x + dx, y));
            if(entry.isValid(x, y + dy)) successors.add(pool.obtain().set(x, y + dy));
            if(entry.isValid(x + dx, y + dy)) successors.add(pool.obtain().set(x + dx, y + dy));

            if(!entry.isValid(x, y + dy) && entry.isValid(x - dx, y + dy)) successors.add(pool.obtain().set(x - dx, y + dy));

            if(!entry.isValid(x + dx, y) && entry.isValid(x + dx, y - dy)) successors.add(pool.obtain().set(x + dx, y - dy));
        }

        return successors;
    }

    /**
     * 递归验证当前点是否是跳点
     * 1.是终点
     * 2.有强迫邻居
     * 3.对角线移动时, 向分量方向探索到跳点
     */
    private Node findJumpPoint(Node current, int dx, int dy, Node goal){
        //是终点
        if((current.x == goal.x && current.y == goal.y)){
            return current;
        }

        if(hasForceNeighbor(current, dx, dy)){
            return current;
        }

        // 对角线方向, 在水平/垂直分量上递归探索跳点
        if(dx != 0 && dy != 0){
            if(findJumpPoint(current, dx, 0, goal) != null || findJumpPoint(current, 0, dy, goal) != null){
                return current;
            }
        }


        int x = current.x + dx;
        int y = current.y + dy;

        if(!entry.isValid(x, y)) return null;

        return findJumpPoint(current, dx, dy, goal);
    }

    /**
     * 检查当前格是否有强迫邻居
     */
    public boolean hasForceNeighbor(Node current, int dx, int dy){
        int x = current.x;
        int y = current.y;

        // 水平移动
        if(dx != 0 && dy == 0){
            // 检查垂直方向的强迫邻居
            if(!entry.isValid(x, y - 1) && entry.isValid(x + dx, y - 1)){
                return true;
            }
            if(!entry.isValid(x, y + 1) && entry.isValid(x + dx, y + 1)){
                return true;
            }
        }
        // 垂直移动
        else if(dx == 0 && dy != 0){
            // 检查水平方向的强迫邻居
            if(!entry.isValid(x - 1, y) && entry.isValid(x - 1, y + dy)){
                return true;
            }
            if(!entry.isValid(x + 1, y) && entry.isValid(x + 1, y + dy)){
                return true;
            }
        }
        // 对角线移动
        else if(dx != 0 && dy != 0){
            // 检查两个对角方向的强迫邻居
            if(!entry.isValid(x - dx, y) && entry.isValid(x - dx, y + dy)){
                return true;
            }
            if(!entry.isValid(x, y - dy) && entry.isValid(x + dx, y - dy)){
                return true;
            }
        }

        return false;
    }

    float heuristic(Node node, int goalX, int goalY){
        return distance(node.x, node.y, goalX, goalY);
    }

    float distance(Node a, Node b){
        return distance(a.x, a.y, b.x, b.y);
    }

    float distance(int x1, int y1, int x2, int y2){
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
//        return Math.max(dx, dy) + ((float)Math.sqrt(2) - 1) * Math.min(dx, dy);
        float cost = 0;

        // 计算路径上所有格子的代价总和
        if (dx > dy) {
            for (int i = 1; i <= dx; i++) {
                cost += entry.cost(x1 + (i * (x2 > x1 ? 1 : -1)), y1);
            }
        } else {
            for (int i = 1; i <= dy; i++) {
                cost += entry.cost(x1, y1 + (i * (y2 > y1 ? 1 : -1)));
            }
        }

        return cost;
    }

    private Array<Vector2> reconstructPath(Node goal){
        Array<Vector2> path = new Array<>();
        Node current = goal;

        // 从终点回溯到起点
        while(current != null){
            path.add(new Vector2(current.x, current.y));
            current = current.parent;
        }

        // 反转路径，使其从起点到终点
        path.reverse();

        return path;
    }

    public static Pool<Node> pool = new Pool<>(128, 1024){
        @Override
        protected Node newObject(){
            return new Node();
        }
    };

    public static class Node implements Pool.Poolable{
        public int x, y;
        public float g, h, f;
        public Node parent;

        public Node(){}

        public Node set(int x, int y){
            this.x = x;
            this.y = y;
            return this;
        }

        @Override
        public void reset(){
            x = y = 0;
            g = h = f = 0;
            parent = null;
        }

        @Override
        public boolean equals(Object obj){
            if(obj == this) return true;
            if(obj == null) return false;
            if(obj.getClass() != getClass()) return false;
            Node other = (Node)obj;
            return x == other.x && y == other.y;
        }
    }

    protected static Comparator<Node> comparator = new Comparator<Node>(){
        @Override
        public int compare(Node o1, Node o2){
            return Float.compare(o1.f, o2.f);
        }
    };
}
