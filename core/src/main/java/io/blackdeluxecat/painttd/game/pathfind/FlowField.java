package io.blackdeluxecat.painttd.game.pathfind;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.map.Map;

import java.util.*;

public class FlowField{
    public Map map;

    public Node[][] nodes;

    public PathfindMapEntry entry = new PathfindMapEntry(){};

    private final PriorityQueue<Node> openList;

    private final int[] d4x = {1, 0, -1, 0};
    private final int[] d4y = {0, 1, 0, -1};


    public FlowField(Map map){
        this.map = map;
        nodes = new Node[map.width][map.height];
        openList = new PriorityQueue<>(comparator);
    }

    /**流场重新生成*/
    public void fullUpdate(){
        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                nodes[x][y] = Node.obtain();
                nodes[x][y].x = x;
                nodes[x][y].y = y;
                nodes[x][y].cost = entry.cost(x, y);
                nodes[x][y].stackCost = Float.MAX_VALUE;
            }
        }
        //终点
        Node goal = nodes[map.width / 2][map.height / 2];
        goal.cost = 0;
        goal.stackCost = 0;
        goal.parent = null;

        //脏标记终点的邻居(排除其他终点)
        for(int i = 0; i < 4; i++){
            int nx = goal.x + d4x[i];
            int ny = goal.y + d4y[i];
            if(!entry.isValid(nx, ny)) continue;
            dirty(nx, ny);
        }

        //启动扩散
        update();
    }

    /**
     * 扩散. 尝试更新自身, 如果积分更新, 就将所有邻居加入openList
     * 父节点应该不会被循环更新积分
     */
    public void update(){
        while(!openList.isEmpty()){
            Node current = openList.poll();
            current.cost = entry.cost(current.x, current.y);

            boolean updated = false;

            for(int i = 0; i < 4; i++){
                int nx = current.x + d4x[i];
                int ny = current.y + d4y[i];
                if(!entry.isValid(nx, ny)) continue;

                Node neighbor = nodes[nx][ny];
                float newCost = neighbor.stackCost + current.cost;

                if(newCost < current.stackCost){
                    current.stackCost = newCost;
                    current.parent = neighbor;
                    updated = true;
                }
            }

            if(updated){
                for(int i = 0; i < 4; i++){
                    int nx = current.x + d4x[i];
                    int ny = current.y + d4y[i];
                    if(!entry.isValid(nx, ny)) continue;
                    openList.add(nodes[nx][ny]);
                }
            }
        }

        flow();
    }

    /**生成流场方向*/
    public void flow(){
        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                Node node = nodes[x][y];
                if(node.parent == null) continue;
                node.direction.set(node.parent.x - x, node.parent.y - y).nor();
            }
        }
    }

    /**标记变化的节点为脏, 加入openList
     * 在循环中, 脏节点通常会更新积分, 导致邻居被标记为脏, 从而传播更新. 如果遇到了不更新的邻居, 自然停止传播.
     */
    public void dirty(int changedX, int changedY){
        Node changed = nodes[changedX][changedY];
        if(openList.contains(changed)) return;
        openList.add(changed);
    }

    public Vector2 getDirection(int x, int y, Vector2 out){
        return out.set(nodes[x][y].direction);
    }

    protected static Comparator<Node> comparator = new Comparator<Node>(){
        @Override
        public int compare(Node o1, Node o2){
            return Float.compare(o1.stackCost, o2.stackCost);
        }
    };

    public static class Node implements Pool.Poolable{
        public static Pool<Node> pool = new Pool<>(){
            @Override
            protected Node newObject(){
                return new Node();
            }
        };

        public static Node obtain(){
            return pool.obtain();
        }

        public int x, y;
        public float cost, stackCost;
        public Node parent;
        public Vector2 direction = new Vector2();

        @Override
        public void reset(){
            x = y = 0;
            cost = stackCost = 0;
            parent = null;
        }
    }
}
