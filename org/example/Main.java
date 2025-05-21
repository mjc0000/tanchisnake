package org.example;

import Game.CheatsFrame;
import Game.GameFrame;
import Game.GamePanel; // 导入 GamePanel
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        boolean playAgain = true;

        while (playAgain) {
            GameFrame game = new GameFrame();
            game.setDefaultCloseOperation(GameFrame.DISPOSE_ON_CLOSE);

            // 获取 GamePanel 实例
            GamePanel gamePanel = (GamePanel) game.getContentPane().getComponent(0);

            // 创建并显示外挂窗口
            CheatsFrame cheatsFrame = new CheatsFrame(gamePanel);

            // 监听主窗口关闭，关闭外挂窗口
            game.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    cheatsFrame.dispose();
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

            if (choice == JOptionPane.NO_OPTION) {
                playAgain = false;
            }
        }

        System.out.println("游戏结束!");
    }
}