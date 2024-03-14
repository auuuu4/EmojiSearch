package cn.m2on;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: m2on
 * @Date: 2024/03/09/21:45
 * @Description:
 */
public class SwingTester {
    public static void main(String[] args) {
        createWindow();
    }

    private static void createWindow() {
        JFrame frame = new JFrame("Swing Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI(frame);
        frame.setSize(560, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createUI(final JFrame frame){
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);



        ImageIcon icon = new ImageIcon("img/settingIco.png");
        JButton iconButton = new JButton(icon);
        iconButton.setText("Next");
        iconButton.setToolTipText("Move Ahead");
        iconButton.setVerticalTextPosition(AbstractButton.CENTER);
        iconButton.setHorizontalTextPosition(AbstractButton.LEADING);
        iconButton.setMnemonic(KeyEvent.VK_I);
        iconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Icon Button clicked.");
            }
        });
//        iconButton.setSize(new Dimension(100,100));
        iconButton.setPreferredSize(new Dimension(100,100));
        panel.add(iconButton);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        System.out.println();
    }
}
//更多请阅读：https://www.yiibai.com/swingexamples/create_button_with_icon_text.html


