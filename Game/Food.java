package Game;

import java.awt.*; // 引入 AWT 组件
import java.util.Random; // 引入 Random 类，用于生成随机数

public class Food { // 定义 Food 类
    private Point position; // 食物的位置
    private final int screenWidth; // 屏幕宽度
    private final int screenHeight; // 屏幕高度
    private final int unitSize; // 游戏单元的大小
    private final Random random; // 随机数生成器

    public Food(int screenWidth, int screenHeight, int unitSize) { // 构造方法，传入屏幕宽度、高度和单元大小
        this.screenWidth = screenWidth; // 设置屏幕宽度
        this.screenHeight = screenHeight; // 设置屏幕高度
        this.unitSize = unitSize; // 设置单元大小
        this.random = new Random(); // 创建随机数生成器
        position = generateNewFood(); // 生成新的食物
    }

    public Point generateNewFood() { // 生成新的食物
        int x = random.nextInt(screenWidth / unitSize) * unitSize; // 生成随机的 x 坐标
        int y = random.nextInt(screenHeight / unitSize) * unitSize; // 生成随机的 y 坐标
        position = new Point(x, y); // 创建新的食物位置
        return position; // 返回食物位置
    }

    public void draw(Graphics g) { // 绘制食物
        g.setColor(Color.red); // 设置颜色为红色
        g.fillOval(position.x, position.y, unitSize, unitSize); // 绘制圆形食物
    }

    public Point getPosition() { // 获取食物的位置
        return position; // 返回食物的位置
    }
}