package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

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

    private boolean isBattleMode = false;
    private int aiDifficulty = 0;

    private Snake aiSnake = null;
    private Color foodColor = Color.RED;
    private Random foodColorRandom = new Random();
    private static final Color[] FOOD_COLORS = new Color[128];
    static {
        for (int i = 0; i < 128; i++) {
            // 生成128种不同的颜色
            int r = (i * 2) % 256;
            int g = (i * 5) % 256;
            int b = (i * 7) % 256;
            FOOD_COLORS[i] = new Color(r, g, b);
        }
    }

    private String winner = null;

    public GamePanel() {
        this(false, 0);
    }

    public GamePanel(boolean isBattleMode, int aiDifficulty) {
        this.isBattleMode = isBattleMode;
        this.aiDifficulty = aiDifficulty;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        initGame();
    }

    public void initGame() {
        if (isBattleMode) {
            // 玩家蛇左上角，身体向右
            snake = new Snake(UNIT_SIZE, Color.GREEN, new Color(45, 180, 0), new Point(0, 0), 'R');
            // AI蛇屏幕中央，身体向左延伸，初始方向向右
            int aiStartX = SCREEN_WIDTH / 2;
            int aiStartY = SCREEN_HEIGHT / 2;
            aiSnake = new Snake(UNIT_SIZE, new Color(255, 80, 80), new Color(180, 0, 0), new Point(aiStartX, aiStartY), 'R');
            // 对战模式下外挂状态保持
        } else {
            // 经典模式玩家蛇头在中央偏左，身体向右延伸
            int startX = SCREEN_WIDTH / 2 - UNIT_SIZE * 2;
            int startY = SCREEN_HEIGHT / 2;
            snake = new Snake(UNIT_SIZE, Color.GREEN, new Color(45, 180, 0), new Point(startX, startY), 'R');
            // 经典模式下只关闭无限长度和无敌功能，保持穿墙功能
            wallPassEnabled = true;
            infiniteLengthEnabled = false;
            invincibleEnabled = false;
        }
        food = new Food(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE);
        collisionDetector = new CollisionDetector(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, this); // 传递 GamePanel 实例
        scoreManager = new ScoreManager();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        food.generateNewFood();
        randomizeFoodColor();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !paused) {
            boolean playerDead = false;
            boolean aiDead = false;
            // 玩家蛇移动
            snake.move();
            // AI蛇移动
            if (isBattleMode && aiSnake != null) {
                char aiDir;
                if (aiDifficulty == 0) { // 简单
                    aiDir = aiSnakeDirectionSimple(aiSnake, snake);
                } else if (aiDifficulty == 1) { // 普通
                    aiDir = aiSnakeDirectionNormal(aiSnake, food.getPosition(), snake);
                } else { // 困难
                    aiDir = aiSnakeDirectionHard(aiSnake, food.getPosition(), snake);
                }
                aiSnake.setDirection(aiDir);
                aiSnake.move();
            }
            // 玩家穿墙
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
            // AI蛇不能穿墙，若撞墙直接判定死亡
            if (isBattleMode && aiSnake != null) {
                if (collisionDetector.isCollisionWithWall(aiSnake.getHead())) {
                    aiDead = true;
                }
            }
            // 玩家吃到食物
            if (collisionDetector.isCollisionWithFood(snake.getHead(), food.getPosition())) {
                snake.eatFood();
                food.generateNewFood();
                scoreManager.increaseScore();
                if (infiniteLengthEnabled) {
                    snake.setInfiniteLength(true);
                } else {
                    snake.setInfiniteLength(false);
                }
                randomizeFoodColor(); // 新食物颜色
            }
            // AI蛇吃到食物
            if (isBattleMode && aiSnake != null && collisionDetector.isCollisionWithFood(aiSnake.getHead(), food.getPosition())) {
                aiSnake.eatFood();
                food.generateNewFood();
                randomizeFoodColor();
            }
            // 玩家无敌/自撞判定
            if (!invincibleEnabled) {
                if ((!wallPassEnabled && collisionDetector.isCollisionWithWall(snake.getHead())) || collisionDetector.isCollisionWithSelf(snake)) {
                    playerDead = true;
                }
            }
            // AI蛇自撞
            if (isBattleMode && aiSnake != null && collisionDetector.isCollisionWithSelf(aiSnake)) {
                aiDead = true;
            }
            // 对战模式下的互相碰撞判定
            if (isBattleMode && aiSnake != null) {
                // 玩家蛇头撞AI蛇身
                if (collisionDetector.isCollisionWithOtherSnake(snake, aiSnake)) {
                    playerDead = true;
                }
                // AI蛇头撞玩家蛇身
                if (collisionDetector.isCollisionWithOtherSnake(aiSnake, snake)) {
                    aiDead = true;
                }
            }
            // 统一判定胜负
            if (isBattleMode && aiSnake != null && (playerDead || aiDead)) {
                running = false;
                if (playerDead && aiDead) {
                    winner = "Draw!";
                } else if (playerDead) {
                    winner = "AI Win!";
                } else if (aiDead) {
                    winner = "Player Win!";
                }
            } else if (!isBattleMode && playerDead) {
                running = false;
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

            // 食物用foodColor
            g.setColor(foodColor);
            Point foodPos = food.getPosition();
            g.fillOval(foodPos.x, foodPos.y, UNIT_SIZE, UNIT_SIZE);

            snake.draw(g);
            if (isBattleMode && aiSnake != null) {
                aiSnake.draw(g);
            }
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

        // 对战模式下显示胜者
        if (isBattleMode && winner != null) {
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 50));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString(winner, (SCREEN_WIDTH - metrics2.stringWidth(winner)) / 2, SCREEN_HEIGHT / 2 + 80);
        }
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

    // 每次生成新食物时调用
    private void randomizeFoodColor() {
        foodColor = FOOD_COLORS[foodColorRandom.nextInt(128)];
    }

    // 简单AI：只要能走就行，优先U/D/L/R顺序
    private char aiSnakeDirectionSimple(Snake aiSnake, Snake playerSnake) {
        char[] dirs = {'U', 'D', 'L', 'R'};
        for (char dir : dirs) {
            if (!willCollide(aiSnake, dir, playerSnake)) {
                return dir;
            }
        }
        return 'R'; // 没路可走只能撞
    }
    // 普通AI：在所有安全方向中选距离食物最近的
    private char aiSnakeDirectionNormal(Snake aiSnake, Point foodPos, Snake playerSnake) {
        Point aiHead = aiSnake.getHead();
        char[] dirs = {'U', 'D', 'L', 'R'};
        char bestDir = 'R';
        int minDist = Integer.MAX_VALUE;
        for (char dir : dirs) {
            if (!willCollide(aiSnake, dir, playerSnake)) {
                Point next = nextPoint(aiHead, dir, aiSnake.getUnitSize());
                int dist = Math.abs(next.x - foodPos.x) + Math.abs(next.y - foodPos.y);
                if (dist < minDist) {
                    minDist = dist;
                    bestDir = dir;
                }
            }
        }
        return bestDir;
    }
    // 困难难度AI：优先吃食物，主动避开碰撞，优先不撞墙和身体，尝试围堵玩家
    private char aiSnakeDirectionHard(Snake aiSnake, Point foodPos, Snake playerSnake) {
        Point aiHead = aiSnake.getHead();
        Point playerHead = playerSnake.getHead();
        char[] dirs = {'U', 'D', 'L', 'R'};
        char bestDir = dirs[0];
        char safeDir = bestDir;
        int minScore = Integer.MAX_VALUE;
        for (char dir : dirs) {
            if (!willCollide(aiSnake, dir, playerSnake)) {
                Point next = nextPoint(aiHead, dir, aiSnake.getUnitSize());
                int distToFood = Math.abs(next.x - foodPos.x) + Math.abs(next.y - foodPos.y);
                int distToPlayer = Math.abs(next.x - playerHead.x) + Math.abs(next.y - playerHead.y);
                int space = countFreeSpace(next, aiSnake, playerSnake);
                int score = distToFood - space + (distToPlayer < 3 ? 10 : 0);
                int aiToFood = Math.abs(aiHead.x - foodPos.x) + Math.abs(aiHead.y - foodPos.y);
                int playerToFood = Math.abs(playerHead.x - foodPos.x) + Math.abs(playerHead.y - foodPos.y);
                if (playerToFood < aiToFood && distToPlayer < 4) {
                    score -= 8;
                }
                if (score < minScore) {
                    minScore = score;
                    safeDir = dir;
                }
            }
        }
        return safeDir;
    }
    // 统计以next为起点的可用空间（简单BFS，步数限制）
    private int countFreeSpace(Point start, Snake aiSnake, Snake playerSnake) {
        int maxStep = 20;
        boolean[][] visited = new boolean[SCREEN_WIDTH / UNIT_SIZE][SCREEN_HEIGHT / UNIT_SIZE];
        ArrayList<Point> queue = new ArrayList<>();
        queue.add(start);
        int count = 0;
        while (!queue.isEmpty() && count < maxStep) {
            Point p = queue.remove(0);
            int gx = p.x / UNIT_SIZE, gy = p.y / UNIT_SIZE;
            if (gx < 0 || gx >= SCREEN_WIDTH / UNIT_SIZE || gy < 0 || gy >= SCREEN_HEIGHT / UNIT_SIZE) continue;
            if (visited[gx][gy]) continue;
            visited[gx][gy] = true;
            // 蛇身不能走
            if (contains(aiSnake.getBody(), p) || contains(playerSnake.getBody(), p)) continue;
            count++;
            // 四方向扩展
            queue.add(new Point(p.x + UNIT_SIZE, p.y));
            queue.add(new Point(p.x - UNIT_SIZE, p.y));
            queue.add(new Point(p.x, p.y + UNIT_SIZE));
            queue.add(new Point(p.x, p.y - UNIT_SIZE));
        }
        return count;
    }
    private boolean contains(java.util.List<Point> list, Point p) {
        for (Point q : list) if (q.equals(p)) return true;
        return false;
    }
    // 判断AI蛇朝某方向走会不会撞到自己或玩家
    private boolean willCollide(Snake aiSnake, char dir, Snake playerSnake) {
        Point head = aiSnake.getHead();
        Point next = nextPoint(head, dir, aiSnake.getUnitSize());
        // 撞墙
        if (next.x < 0 || next.x >= SCREEN_WIDTH || next.y < 0 || next.y >= SCREEN_HEIGHT) return true;
        // 撞自己
        for (Point p : aiSnake.getBody()) {
            if (p.equals(next)) return true;
        }
        // 撞玩家身体
        for (int i = 1; i < playerSnake.getBody().size(); i++) {
            if (playerSnake.getBody().get(i).equals(next)) return true;
        }
        return false;
    }
    // 计算某方向的下一个点
    private Point nextPoint(Point head, char dir, int unitSize) {
        switch (dir) {
            case 'U': return new Point(head.x, head.y - unitSize);
            case 'D': return new Point(head.x, head.y + unitSize);
            case 'L': return new Point(head.x - unitSize, head.y);
            case 'R': return new Point(head.x + unitSize, head.y);
        }
        return head;
    }
}
