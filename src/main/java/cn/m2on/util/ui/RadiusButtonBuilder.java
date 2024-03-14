package cn.m2on.util.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * @Author: m2on
 * @Date: 2024/03/13/9:15
 * @Description:
 */
public class RadiusButtonBuilder {
    /**
     * 透明色
     */
    static final Color NO_COLOR = new Color(255, 255, 255, 0);
    private static class RadiusButton extends JButton{
        Color color;
        int radius;
        RadiusButton(String text,Color color,int radius){
            super(text);
            super.setContentAreaFilled(false);
            this.setBorder(RadiusBorderBuilder.createRoundBorder(color,radius));
            this.color = color;
            this.radius = radius;

        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, super.getSize().width - 1, super.getSize().height - 1, radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }
    public static JButton createRadiusButton(String text, Color color, int radius){
        return new RadiusButton(text,color,radius);
    }
    public static void changeColor(Color color,RadiusButton button){
        button.color = color;
    }

    private static RadiusButton parse(JButton jButton){
        return (RadiusButton) jButton;
    }

    public static void registerButtonTouchEven(JButton button,Color enter,Color exit,Color press) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {  //鼠标移上去
                RadiusButtonBuilder.changeColor(enter, RadiusButtonBuilder.parse(button));
            }

            @Override
            public void mouseExited(MouseEvent e) {  //鼠标离开
                RadiusButtonBuilder.changeColor(exit, RadiusButtonBuilder.parse(button));

            }
            @Override
            public void mousePressed(MouseEvent e) { // 鼠标按下
                RadiusButtonBuilder.changeColor(press, RadiusButtonBuilder.parse(button));
            }

            @Override
            public void mouseReleased(MouseEvent e) { // 鼠标松开
                RadiusButtonBuilder.changeColor(exit, RadiusButtonBuilder.parse(button));
            }
        });
    }

}
