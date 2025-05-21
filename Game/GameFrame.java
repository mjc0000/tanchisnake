package Game;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        this(false, 0);
    }

    public GameFrame(boolean isBattleMode, int aiDifficulty) {
        this.add(new GamePanel(isBattleMode, aiDifficulty));

        this.setTitle("贪吃蛇");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出窗口关闭程序。
        this.setResizable(false);//禁止调整窗口大小。
        this.pack();//根据内容自动调整窗口。
        this.setVisible(true);//窗口可见
        this.setLocationRelativeTo(null);//窗口居中显示


    }

    public static void main(String[] args) {
        new GameFrame();
    }


    @Override
    public boolean isDisplayable() {
        return super.isDisplayable();
    }

}
