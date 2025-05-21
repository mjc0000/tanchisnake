package Game;

import java.awt.*;
import java.util.ArrayList;

public class Snake {

    private ArrayList<Point> body;
    private char direction = 'R';
    private final int unitSize;
    private boolean infiniteLength = false;

    public Snake(int unitSize) {
        this.unitSize = unitSize;
        body = new ArrayList<>();
        body.add(new Point(0, 0)); // Initial head position
        for (int i = 1; i < 6; i++) {
            body.add(new Point(-i * unitSize, 0)); // Initial body segments
        }
    }

    public void move() {
        Point head = body.get(0);
        Point newHead = null;

        switch (direction) {
            case 'U':
                newHead = new Point(head.x, head.y - unitSize);
                break;
            case 'D':
                newHead = new Point(head.x, head.y + unitSize);
                break;
            case 'L':
                newHead = new Point(head.x - unitSize, head.y);
                break;
            case 'R':
                newHead = new Point(head.x + unitSize, head.y);
                break;
        }

        body.add(0, newHead);  // Add new head
        if (!infiniteLength) {
            body.remove(body.size() - 1); // Remove the tail
        }
    }

    public void eatFood() {
        Point head = body.get(0);
        Point newHead = null;

        switch (direction) {
            case 'U':
                newHead = new Point(head.x, head.y - unitSize);
                break;
            case 'D':
                newHead = new Point(head.x, head.y + unitSize);
                break;
            case 'L':
                newHead = new Point(head.x - unitSize, head.y);
                break;
            case 'R':
                newHead = new Point(head.x + unitSize, head.y);
                break;
        }
        body.add(0, newHead);
    }
    public Point getHead(){
        return body.get(0);
    }

    public boolean checkCollisionWithSelf() {
        Point head = body.get(0);
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g) {
        for (int i = 0; i < body.size(); i++) {
            if (i == 0) {
                g.setColor(Color.green); // Head color
            } else {
                g.setColor(new Color(45, 180, 0)); // Body color
            }
            g.fillRect(body.get(i).x, body.get(i).y, unitSize, unitSize);
        }
    }

    public void setDirection(char direction) {
        if ((direction == 'L' && this.direction != 'R') ||
                (direction == 'R' && this.direction != 'L') ||
                (direction == 'U' && this.direction != 'D') ||
                (direction == 'D' && this.direction != 'U')) {
            this.direction = direction;
        }
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    public int getUnitSize() {
        return unitSize;
    }
    public void setInfiniteLength(boolean enabled){
        infiniteLength = enabled;
    }
}
/*package Game;


import java.awt.*; // 引入 AWT 组件
import java.util.ArrayList; // 引入 ArrayList 类，用于存储蛇的身体片段

public class Snake { // 定义 Snake 类

    private ArrayList<Point> body; // 蛇的身体片段，使用 ArrayList 存储 Point 对象
    private char direction = 'R'; // 蛇的移动方向，'R' 表示右，'L' 表示左，'U' 表示上，'D' 表示下
    private final int unitSize; // 游戏单元的大小

    public Snake(int unitSize) { // 构造方法，传入单元大小
        this.unitSize = unitSize; // 设置单元大小
        body = new ArrayList<>(); // 创建 ArrayList 对象，用于存储蛇的身体片段
        body.add(new Point(0, 0)); // 初始化蛇头的位置
        for (int i = 1; i < 6; i++) { // 初始化蛇的身体
            body.add(new Point(-i * unitSize, 0)); // 添加身体片段到蛇的身体
        }
    }

    public void move() { // 移动蛇
        Point head = body.get(0); // 获取蛇头
        Point newHead = null; // 新的蛇头

        switch (direction) { // 根据蛇的方向计算新的蛇头位置
            case 'U': // 向上移动
                newHead = new Point(head.x, head.y - unitSize); // 新的蛇头位置在当前蛇头的上方
                break;
            case 'D': // 向下移动
                newHead = new Point(head.x, head.y + unitSize); // 新的蛇头位置在当前蛇头的下方
                break;
            case 'L': // 向左移动
                newHead = new Point(head.x - unitSize, head.y); // 新的蛇头位置在当前蛇头的左侧
                break;
            case 'R': // 向右移动
                newHead = new Point(head.x + unitSize, head.y); // 新的蛇头位置在当前蛇头的右侧
                break;
        }

        body.add(0, newHead);  // Add new head - 添加新的蛇头
        body.remove(body.size() - 1); // Remove the tail - 移除蛇尾
    }

    public void eatFood() { // 蛇吃食物
        Point head = body.get(0); // 获取蛇头
        Point newHead = null; // 新的蛇头

        switch (direction) { // 根据蛇的方向计算新的蛇头位置
            case 'U': // 向上移动
                newHead = new Point(head.x, head.y - unitSize); // 新的蛇头位置在当前蛇头的上方
                break;
            case 'D': // 向下移动
                newHead = new Point(head.x, head.y + unitSize); // 新的蛇头位置在当前蛇头的下方
                break;
            case 'L': // 向左移动
                newHead = new Point(head.x - unitSize, head.y); // 新的蛇头位置在当前蛇头的左侧
                break;
            case 'R': // 向右移动
                newHead = new Point(head.x + unitSize, head.y); // 新的蛇头位置在当前蛇头的右侧
                break;
        }
        body.add(0, newHead); // 添加新的蛇头，蛇的长度增加
    }

    public Point getHead(){ // 获取蛇头
        return body.get(0); // 返回蛇头
    }

    public boolean checkCollisionWithSelf() { // 检查是否撞到自身
        Point head = body.get(0); // 获取蛇头
        for (int i = 1; i < body.size(); i++) { // 循环遍历蛇的身体
            if (head.equals(body.get(i))) { // 如果蛇头和身体的某个片段重合
                return true; // 撞到自身
            }
        }
        return false; // 没有撞到自身
    }

    public void draw(Graphics g) { // 绘制蛇
        for (int i = 0; i < body.size(); i++) { // 循环遍历蛇的身体
            if (i == 0) { // 如果是蛇头
                g.setColor(Color.green); // 设置颜色为绿色
            } else { // 如果是蛇身
                g.setColor(new Color(45, 180, 0)); // 设置颜色为深绿色
            }
            g.fillRect(body.get(i).x, body.get(i).y, unitSize, unitSize); // 绘制蛇的片段
        }
    }

    public void setDirection(char direction) { // 设置蛇的方向
        if ((direction == 'L' && this.direction != 'R') || // 如果要向左移动，并且当前方向不是向右
                (direction == 'R' && this.direction != 'L') || // 如果要向右移动，并且当前方向不是向左
                (direction == 'U' && this.direction != 'D') || // 如果要向上移动，并且当前方向不是向下
                (direction == 'D' && this.direction != 'U')) { // 如果要向下移动，并且当前方向不是向上
            this.direction = direction; // 设置蛇的方向
        }
    }

    public ArrayList<Point> getBody() { // 获取蛇的身体
        return body; // 返回蛇的身体
    }

    public int getUnitSize() { // 获取游戏单元的大小
        return unitSize; // 返回游戏单元的大小
    }
}


 */