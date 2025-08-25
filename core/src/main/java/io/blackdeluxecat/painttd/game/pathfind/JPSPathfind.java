package io.blackdeluxecat.painttd.game.pathfind;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.game.*;

import java.util.*;

public class JPSPathfind{
    public interface JPSMapEntry{
        default boolean isValidPosition(int x, int y){
            var tile = Game.map.get(x, y);
            return tile != null && !tile.isWall;
        }

        default float cost(int x, int y){
            var tile = Game.map.get(x, y);
            return tile == null ? Float.MAX_VALUE : tile.layer;
        }
    }

    public JPSMapEntry entry = new JPSMapEntry(){};

    // 主寻路函数
    public Array<Vector2> findPath(float startX, float startY, float targetX, float targetY){
        // 1. 转换为网格坐标
        int startGridX = (int)startX;
        int startGridY = (int)startY;
        int goalGridX = (int)targetX;
        int goalGridY = (int)targetY;

        // 2. 初始化
        PriorityQueue<Node> openList = new PriorityQueue<>(comparator);
        ObjectSet<Node> closedList = new ObjectSet<>();
        Node start = new Node(startGridX, startGridY);

        // 3. 添加起点
        openList.add(start);

        // 4. 主循环
        while(!openList.isEmpty()){
            Node current = openList.poll();

            // 找到目标, 构建完整路径
            if(current.x == goalGridX && current.y == goalGridY){
                return reconstructPath(current);
            }

            //探索可能的跳点
            Array<Node> neighbors = findSuccessors(current, goalGridX, goalGridY);

            for(Node neighbor : neighbors){
                if(closedList.contains(neighbor)){
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

        return null; // 没有找到路径
    }

    /**
     * 从当前点向周围探索跳点
     * 如果为起点(无父节点), 全向探索
     */
    private Array<Node> findSuccessors(Node node, int goalX, int goalY){
        Array<Node> successors = new Array<>();
        int x = node.x;
        int y = node.y;

        // 如果是起点，进行全方向探索
        if(node.parent == null){
            for(int dx = -1; dx <= 1; dx++){
                for(int dy = -1; dy <= 1; dy++){
                    if(dx == 0 && dy == 0) continue;

                    Node jumpPoint = findJumpPoint(node, dx, dy, new Node(goalX, goalY));
                    if(jumpPoint != null){
                        successors.add(jumpPoint);
                    }
                }
            }
            return successors;
        }

        // 获取移动方向
        int dx = Integer.compare(x, node.parent.x);
        int dy = Integer.compare(y, node.parent.y);

        Node jumpPoint = findJumpPoint(node, dx, dy, new Node(goalX, goalY));
        if(jumpPoint != null){
            successors.add(jumpPoint);
        }

        return successors;
    }

    /**
     * 检查当前点是否为跳点
     * 1.终点总是跳点
     * 2.有强迫邻居的是跳点
     * 3.对角线移动时, 如果分量方向探索到跳点, 则自身是跳点
     */
    private Node findJumpPoint(Node current, int dx, int dy, Node goal){
        int x = current.x + dx;
        int y = current.y + dy;

        // 前方障碍物
        if(!entry.isValidPosition(x, y)){
            return null;
        }

        //终点总是跳点
        //起点已在第一次循环中被加入
        if((current.x == goal.x && current.y == goal.y)){
            return new Node(x, y);
        }

        if(hasForceNeighbor(current, dx, dy)){
            return new Node(x, y);
        }

        // 对角线方向
        if(dx != 0 && dy != 0){
            // 水平/垂直分量, 递归探索跳点
            if(findJumpPoint(new Node(x, y), dx, 0, goal) != null || findJumpPoint(new Node(x, y), 0, dy, goal) != null){
                //探索到跳点时, 自身也是跳点
                return new Node(x, y);
            }
        }

        return null;
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
            if(!entry.isValidPosition(x, y - 1) && entry.isValidPosition(x + dx, y - 1)){
                return true;
            }
            if(!entry.isValidPosition(x, y + 1) && entry.isValidPosition(x + dx, y + 1)){
                return true;
            }
        }
        // 垂直移动
        else if(dx == 0 && dy != 0){
            // 检查水平方向的强迫邻居
            if(!entry.isValidPosition(x - 1, y) && entry.isValidPosition(x - 1, y + dy)){
                return true;
            }
            if(!entry.isValidPosition(x + 1, y) && entry.isValidPosition(x + 1, y + dy)){
                return true;
            }
        }
        // 对角线移动
        else if(dx != 0 && dy != 0){
            // 检查两个对角方向的强迫邻居
            if(!entry.isValidPosition(x - dx, y) && entry.isValidPosition(x - dx, y + dy)){
                return true;
            }
            if(!entry.isValidPosition(x, y - dy) && entry.isValidPosition(x + dx, y - dy)){
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
        return Math.max(dx, dy) + ((float)Math.sqrt(2) - 1) * Math.min(dx, dy);
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

    public static class Node{
        public int x, y;
        public float g, h, f;
        public Node parent;

        public Node(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    protected static Comparator<Node> comparator = new Comparator<Node>(){
        @Override
        public int compare(Node o1, Node o2){
            return Float.compare(o1.f, o2.f);
        }
    };
}
