package Game;

import java.awt.*; // 引入 AWT 组件
import java.util.ArrayList; // 引入 ArrayList 类

public class CollisionDetector { // 定义 CollisionDetector 类
    private final int screenWidth; // 屏幕宽度
    private final int screenHeight; // 屏幕高度
    private final int unitSize; // 游戏单元的大小
    private boolean wallPassEnabled;

    public CollisionDetector(int screenWidth, int screenHeight, int unitSize, GamePanel gamePanel) { // 构造方法，传入屏幕宽度、高度和单元大小
        this.screenWidth = screenWidth; // 设置屏幕宽度
        this.screenHeight = screenHeight; // 设置屏幕高度
        this.unitSize = unitSize; // 设置单元大小
    }

    public boolean isCollisionWithFood(Point head, Point food) { // 检测是否与食物碰撞
        return head.equals(food); // 如果蛇头和食物的位置相同，则发生碰撞
    }

    public boolean isCollisionWithWall(Point head) { // 检测是否与墙壁碰撞
        return head.x < 0 || head.x >= screenWidth || head.y < 0 || head.y >= screenHeight; // 如果蛇头超出屏幕边界，则发生碰撞
    }

    public void setWallPassEnabled(boolean wallPassEnabled) {
        this.wallPassEnabled = wallPassEnabled;
    }

    public boolean isCollisionWithSelf(Snake snake) { // 检测是否与自身碰撞
        ArrayList<Point> body = snake.getBody(); // 获取蛇的身体
        Point head = body.get(0); // 获取蛇头
        for (int i = 1; i < body.size(); i++) { // 遍历蛇的身体
            if (head.equals(body.get(i))) { // 如果蛇头和身体的某个片段重合
                return true; // 撞到自身
            }
        }
        return false; // 没有撞到自身
    }

    // 检测snake1的头是否撞到snake2的身体
    public boolean isCollisionWithOtherSnake(Snake snake1, Snake snake2) {
        Point head = snake1.getHead();
        ArrayList<Point> body = snake2.getBody();
        // 不包括对方蛇头
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }
}