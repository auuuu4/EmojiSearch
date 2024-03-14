package cn.m2on;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: m2on
 * @Date: 2024/03/09/15:23
 * @Description:
 */
public class MultipleSquaresExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Multiple Squares Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        ImageIcon icon = new ImageIcon("img/settingIco.png");

        JButton button = new JButton(icon);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 使用 BoxLayout 垂直布局
        panel.add(button);

        frame.setVisible(true);
    }

    private static JLabel createSquareLabel(Color color) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(50, 50));
        label.setOpaque(true);
        label.setBackground(color);
        return label;
    }
}
