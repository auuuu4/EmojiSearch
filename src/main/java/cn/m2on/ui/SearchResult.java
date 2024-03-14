package cn.m2on.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: m2on
 * @Date: 2024/03/09/13:04
 * @Description:
 */
public class SearchResult extends JPanel{

    SearchResult() {

//        this.ImageSource = new JPanel();
//        tips = new JLabel("tips：");
        initComponents();
        imageCopyButton = new JButton("获取图片");

//        imageCopyButton.setMaximumSize(new Dimension(100,100));
//        imageCopyButton.setMinimumSize(new Dimension(100,100));
//        this.ImageSource.setLayout(new BorderLayout()); // 设置布局管理器
        add(imageCopyButton);
//        this.ImageSource.add(imageCopyButton, BorderLayout.CENTER); // 添加标题标签
//        this.ImageSource.add(tips, BorderLayout.SOUTH); // 添加标题标签

        imageCopyButton.setPreferredSize(new Dimension(100, 100)); // 设置首选大小
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        imageCopyButton = new JButton();

        //======== this ========
        setLayout(null);

        //---- imageCopyButton ----
        imageCopyButton.setText("Image");
        add(imageCopyButton);
        imageCopyButton.setBounds(0, 0, 250, 210);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < getComponentCount(); i++) {
                Rectangle bounds = getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            setMinimumSize(preferredSize);
            setPreferredSize(preferredSize);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JButton imageCopyButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
