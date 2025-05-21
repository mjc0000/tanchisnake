package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int DELAY = 75;

    Snake snake;
    Food food;
    CollisionDetector collisionDetector;
    ScoreManager scoreManager;
    Timer timer;
    boolean running = false;
    private boolean paused = false;

    // Cheats - 作弊选项
    private boolean wallPassEnabled = true;
    private boolean infiniteLengthEnabled = false;
    private boolean invincibleEnabled = false;


    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        initGame();
    }

    public void initGame() {
        snake = new Snake(UNIT_SIZE);
        food = new Food(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE);
        collisionDetector = new CollisionDetector(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, this); // 传递 GamePanel 实例
        scoreManager = new ScoreManager();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        food.generateNewFood();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !paused) {
            snake.move();
            if (wallPassEnabled) {
                Point head = snake.getHead();
                int x = head.x;
                int y = head.y;
                boolean changed = false;
                if (x < 0) {
                    head.x = SCREEN_WIDTH - UNIT_SIZE;
                    changed = true;
                } else if (x >= SCREEN_WIDTH) {
                    head.x = 0;
                    changed = true;
                }
                if (y < 0) {
                    head.y = SCREEN_HEIGHT - UNIT_SIZE;
                    changed = true;
                } else if (y >= SCREEN_HEIGHT) {
                    head.y = 0;
                    changed = true;
                }
                if (changed) {
                    snake.getBody().set(0, head);
                }
            }
            if (collisionDetector.isCollisionWithFood(snake.getHead(), food.getPosition())) {
                snake.eatFood();
                food.generateNewFood();
                scoreManager.increaseScore();
                if (infiniteLengthEnabled) {
                    snake.setInfiniteLength(true);
                } else {
                    snake.setInfiniteLength(false);
                }
            }
            if (!invincibleEnabled) {
                if ((!wallPassEnabled && collisionDetector.isCollisionWithWall(snake.getHead())) || collisionDetector.isCollisionWithSelf(snake)) {
                    running = false;
                }
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw grid (optional)
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            food.draw(g);
            snake.draw(g);
            scoreManager.drawScore(g, SCREEN_WIDTH);

        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        // Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        // Display score
        scoreManager.drawGameOverScore(g,SCREEN_WIDTH);
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    snake.setDirection('L');
                    break;
                case KeyEvent.VK_RIGHT:
                    snake.setDirection('R');
                    break;
                case KeyEvent.VK_UP:
                    snake.setDirection('U');
                    break;
                case KeyEvent.VK_DOWN:
                    snake.setDirection('D');
                    break;
                case KeyEvent.VK_SPACE:
                    if (!running) return;
                    paused = !paused;
                    if (paused) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                    break;
            }
        }
    }
    // Getter and Setter for WallPass
    public boolean isWallPassEnabled() {
        return wallPassEnabled;
    }

    public void setWallPassEnabled(boolean wallPassEnabled) {
        this.wallPassEnabled = wallPassEnabled;
        collisionDetector.setWallPassEnabled(wallPassEnabled);
    }

    // Getter and Setter for InfiniteLength
    public boolean isInfiniteLengthEnabled() {
        return infiniteLengthEnabled;
    }

    public void setInfiniteLengthEnabled(boolean infiniteLengthEnabled) {
        this.infiniteLengthEnabled = infiniteLengthEnabled;
    }

    // Getter and Setter for Invincible
    public boolean isInvincibleEnabled() {
        return invincibleEnabled;
    }

    public void setInvincibleEnabled(boolean invincibleEnabled) {
        this.invincibleEnabled = invincibleEnabled;
    }
}
/*package Game;


import javax.swing.*; // 引入 Swing 组件
import java.awt.*; // 引入 AWT 组件
import java.awt.event.*; // 引入事件处理类
import java.util.Random; // 引入 Random 类，用于生成随机数

public class GamePanel extends JPanel implements ActionListener { // 定义 GamePanel 类，继承自 JPanel 并实现 ActionListener 接口

    static final int SCREEN_WIDTH = 600; // 屏幕宽度
    static final int SCREEN_HEIGHT = 600; // 屏幕高度
    static final int UNIT_SIZE = 25; // 游戏单元的大小 (蛇的片段、食物)
    static final int DELAY = 75; // 游戏速度 (毫秒)

    Snake snake; // 蛇对象
    Food food; // 食物对象
    CollisionDetector collisionDetector; // 碰撞检测器对象
    ScoreManager scoreManager; // 分数管理器对象
    Timer timer; // 定时器对象，用于控制游戏循环
    boolean running = false; // 游戏是否运行

    GamePanel() { // 构造方法
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // 设置面板的首选大小
        this.setBackground(Color.black); // 设置背景颜色为黑色
        this.setFocusable(true); // 使面板可获得焦点，以便接收键盘事件
        this.addKeyListener(new MyKeyAdapter()); // 添加键盘监听器
        initGame(); // 初始化游戏
    }

    public void initGame() { // 初始化游戏
        snake = new Snake(UNIT_SIZE); // 创建蛇对象，传入单元大小
        food = new Food(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE); // 创建食物对象，传入屏幕宽度、高度和单元大小
        collisionDetector = new CollisionDetector(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE); // 创建碰撞检测器对象，传入屏幕宽度、高度和单元大小
        scoreManager = new ScoreManager(); // 创建分数管理器对象
        running = true; // 设置游戏运行状态为 true
        timer = new Timer(DELAY, this); // 创建定时器，定时触发 actionPerformed 方法
        timer.start(); // 启动定时器
        food.generateNewFood(); // 生成新的食物
    }

    @Override
    public void actionPerformed(ActionEvent e) { // 定时器触发的方法
        if (running) { // 如果游戏正在运行
            snake.move(); // 移动蛇
            if (collisionDetector.isCollisionWithFood(snake.getHead(), food.getPosition())) { // 如果蛇头和食物发生碰撞
                snake.eatFood(); // 蛇吃食物
                food.generateNewFood(); // 生成新的食物
                scoreManager.increaseScore(); // 增加分数
            }
            if (collisionDetector.isCollisionWithWall(snake.getHead()) || collisionDetector.isCollisionWithSelf(snake)) { // 如果蛇撞到墙壁或自身
                running = false; // 游戏结束
            }
        }
        repaint(); // 重新绘制画面
    }

    public void paintComponent(Graphics g) { // 绘制组件
        super.paintComponent(g); // 调用父类的 paintComponent 方法
        draw(g); // 绘制游戏元素
    }

    public void draw(Graphics g) { // 绘制游戏元素
        if (running) { // 如果游戏正在运行
            // Draw grid (optional) - 绘制网格 (可选)
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) { // 循环绘制网格线
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // 绘制垂直线
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // 绘制水平线
            }

            food.draw(g); // 绘制食物
            snake.draw(g); // 绘制蛇
            scoreManager.drawScore(g, SCREEN_WIDTH); // 绘制分数

        } else { // 如果游戏结束
            gameOver(g); // 绘制游戏结束画面
        }
    }

    public void gameOver(Graphics g) { // 绘制游戏结束画面
        // Game Over text - 游戏结束文本
        g.setColor(Color.red); // 设置颜色为红色
        g.setFont(new Font("Ink Free", Font.BOLD, 75)); // 设置字体
        FontMetrics metrics1 = getFontMetrics(g.getFont()); // 获取字体度量
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2); // 绘制 "Game Over"

        // Display score - 显示分数
        scoreManager.drawGameOverScore(g,SCREEN_WIDTH); // 绘制游戏结束时的分数
    }

    public class MyKeyAdapter extends KeyAdapter { // 内部类，用于处理键盘事件
        @Override
        public void keyPressed(KeyEvent e) { // 按键事件
            switch (e.getKeyCode()) { // 获取按键代码
                case KeyEvent.VK_LEFT: // 如果按下左键
                    snake.setDirection('L'); // 设置蛇的方向为左
                    break;
                case KeyEvent.VK_RIGHT: // 如果按下右键
                    snake.setDirection('R'); // 设置蛇的方向为右
                    break;
                case KeyEvent.VK_UP: // 如果按下上键
                    snake.setDirection('U'); // 设置蛇的方向为上
                    break;
                case KeyEvent.VK_DOWN: // 如果按下下键
                    snake.setDirection('D'); // 设置蛇的方向为下
                    break;
            }
        }
    }
}

/*package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.jar.JarEntry;

public class GamePanel extends JPanel implements ActionListener{
    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;//网格单元大小
    static final int DELAY=75; //游戏速度

    Snake snake;
    Food food;
    CollisionDetector collisionDetector;//碰撞检测（对象）
    ScoreManager scoreManager;//分数管理（对象）
    Timer timer;//定时器
    boolean running =false; //检测游戏是否运行


    GamePanel(){ //构造方法
        this.setPreferredSize( new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);//设置背景色
        this.setFocusable(true);//聚焦面板；
        this.addKeyListener(new MykeyAdapter());//窃听键盘
        initGame();//初始化游戏。


    }


    public void initGame(){
        snake=new Snake(UNIT_SIZE);//创建蛇，传入单元大小。
        food =new Food(SCREEN_WIDTH,SCREEN_HEIGHT);//以窗口显示大小散部食物。
        collisionDetector=new CollisionDetector(SCREEN_WIDTH,SCREEN_HEIGHT);//以窗口显示大小部署检测碰撞对象。
        scoreManager =new ScoreManager();
        timer=new Timer(DELAY,this);
        timer.start();
        food.generateNewFood();
    }



}

 */
