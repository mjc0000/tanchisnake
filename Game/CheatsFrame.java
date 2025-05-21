package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheatsFrame extends JFrame {

    private JCheckBox wallPassCheckBox;
    private JCheckBox infiniteLengthCheckBox;
    private JCheckBox invincibleCheckBox;

    private boolean wallPassEnabled = false;
    private boolean infiniteLengthEnabled = false;
    private boolean invincibleEnabled = false;

    public CheatsFrame(GamePanel gamePanel) {
        setTitle("贪吃蛇外挂");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭窗口

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        wallPassCheckBox = new JCheckBox("穿墙");
        infiniteLengthCheckBox = new JCheckBox("无限长度");
        invincibleCheckBox = new JCheckBox("无敌");

        // 读取上次状态
        wallPassCheckBox.setSelected(gamePanel.isWallPassEnabled());
        infiniteLengthCheckBox.setSelected(gamePanel.isInfiniteLengthEnabled());
        invincibleCheckBox.setSelected(gamePanel.isInvincibleEnabled());

        wallPassCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wallPassEnabled = wallPassCheckBox.isSelected();
                gamePanel.setWallPassEnabled(wallPassEnabled);
            }
        });

        infiniteLengthCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infiniteLengthEnabled = infiniteLengthCheckBox.isSelected();
                gamePanel.setInfiniteLengthEnabled(infiniteLengthEnabled);
            }
        });

        invincibleCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invincibleEnabled = invincibleCheckBox.isSelected();
                gamePanel.setInvincibleEnabled(invincibleEnabled);
            }
        });

        panel.add(wallPassCheckBox);
        panel.add(infiniteLengthCheckBox);
        panel.add(invincibleCheckBox);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean isWallPassEnabled() {
        return wallPassEnabled;
    }

    public boolean isInfiniteLengthEnabled() {
        return infiniteLengthEnabled;
    }

    public boolean isInvincibleEnabled() {
        return invincibleEnabled;
    }
}