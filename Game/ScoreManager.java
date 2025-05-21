package Game;

import java.awt.*; // 引入 AWT 组件

public class ScoreManager { // 定义 ScoreManager 类
    private int score = 0; // 分数

    public void increaseScore() { // 增加分数
        score++; // 分数加 1
    }

    public int getScore() { // 获取分数
        return score; // 返回分数
    }

    public void drawScore(Graphics g, int screenWidth) { // 绘制分数
        g.setColor(Color.red); // 设置颜色为红色
        g.setFont(new Font("Ink Free", Font.BOLD, 40)); // 设置字体
        FontMetrics metrics = g.getFontMetrics(g.getFont()); // 获取字体度量
        g.drawString("Score: " + score, (screenWidth - metrics.stringWidth("Score: " + score)) / 2, g.getFont().getSize()); // 绘制分数
    }

    public void drawGameOverScore(Graphics g, int screenWidth){ // 绘制游戏结束时的分数
        g.setColor(Color.red); // 设置颜色为红色
        g.setFont(new Font("Ink Free", Font.BOLD, 40)); // 设置字体
        FontMetrics metrics2 = g.getFontMetrics(g.getFont()); // 获取字体度量
        g.drawString("Score: " + score, (screenWidth - metrics2.stringWidth("Score: " + score)) / 2, g.getFont().getSize()); // 绘制分数
    }
}