package cn.m2on.util.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * @Author: m2on
 * @Date: 2024/03/13/9:54
 * @Description:
 */
public class RadiusBorderBuilder {
    private static JButton DEFAULT_BUTTON = new JButton();

    public static Border createRoundBorder(Color color, int radius) {
        return new RadiusBorderBuilder.RoundBorder(color, radius);
    }

    /**
     * 椭圆边框
     */
    private static class RoundBorder implements Border {

        private Color color;

        private int radius;

        public RoundBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return DEFAULT_BUTTON.getBorder().getBorderInsets(c);
        }

        ;

        @Override
        public boolean isBorderOpaque() {
            return false;
        }


        // 实现Border（父类）方法
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width,
                                int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, radius, radius);
            g2.dispose();
        }

    }
}
