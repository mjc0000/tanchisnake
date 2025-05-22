package org.example;

import Game.CheatsFrame;
import Game.GameFrame;
import Game.GamePanel; // 导入 GamePanel
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        boolean playAgain = true;

        while (playAgain) {
            // 新增：玩法选择
            String[] modes = {"经典", "对战"};
            int mode = JOptionPane.showOptionDialog(null, "请选择玩法", "玩法选择", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, modes, modes[0]);
            boolean isBattleMode = (mode == 1);
            int aiDifficulty = 0;
            if (isBattleMode) {
                String[] levels = {"简单", "普通", "困难"};
                aiDifficulty = JOptionPane.showOptionDialog(null, "请选择AI难度", "难度选择", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, levels, levels[0]);
            }

            GameFrame game = new GameFrame(isBattleMode, aiDifficulty);
            game.setDefaultCloseOperation(GameFrame.DISPOSE_ON_CLOSE);

            // 获取 GamePanel 实例
            GamePanel gamePanel = (GamePanel) game.getContentPane().getComponent(0);

            // 监听Z键弹出外挂窗口
            gamePanel.addKeyListener(new java.awt.event.KeyAdapter() {
                CheatsFrame[] cheatsFrameRef = new CheatsFrame[1];
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_Z) {
                        if (cheatsFrameRef[0] == null || !cheatsFrameRef[0].isDisplayable()) {
                            cheatsFrameRef[0] = new CheatsFrame(gamePanel);
                        } else if (!cheatsFrameRef[0].isVisible() || (cheatsFrameRef[0].getExtendedState() & java.awt.Frame.ICONIFIED) != 0) {
                            cheatsFrameRef[0].setVisible(true);
                            cheatsFrameRef[0].setExtendedState(java.awt.Frame.NORMAL);
                            cheatsFrameRef[0].toFront();
                        } else {
                            cheatsFrameRef[0].setExtendedState(java.awt.Frame.ICONIFIED);
                            // 兜底：如果还没最小化就直接隐藏
                            if (cheatsFrameRef[0].isVisible()) {
                                cheatsFrameRef[0].setVisible(false);
                            }
                        }
                    }
                }
            });
            // 主窗口关闭时也关闭外挂窗口
            game.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    // 关闭外挂窗口
                    // 由于cheatsFrameRef在KeyListener里，需遍历所有窗口
                    for (java.awt.Window w : java.awt.Window.getWindows()) {
                        if (w instanceof CheatsFrame) w.dispose();
                    }
                }
            });

            while (game.isDisplayable()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int choice = JOptionPane.showConfirmDialog(null, "游戏结束! 重新开始?", "重新开始", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            }
        }

        System.out.println("游戏结束!");
    }
}