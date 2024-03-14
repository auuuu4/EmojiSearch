/*
 * Created by JFormDesigner on Thu Mar 14 16:27:10 HKT 2024
 */

package cn.m2on.ui;

import cn.m2on.config.SearchConfig;
import cn.m2on.crawler.ImageThreadPoolExecutor;
import cn.m2on.data.SearchData;
import cn.m2on.util.ui.RadiusButtonBuilder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import static cn.m2on.ui.Index.*;

/**
 * @author Administrator
 */
public class Setting extends JFrame {
    private JFrame parentFrame;
    private Setting settingWin = this;
    private int preX,preY;
    private boolean isDragging;
    public static final Color TEXT_BK_COLOR = new Color(47, 47, 47, 255);

    public Setting(JFrame parent) {
        parentFrame = parent;
        setUndecorated(true);
        registerMouseDraggingFrame(settingWin);
        initComponents();
        manualInitComponents();
    }
    /**
    * @author: auuuu4
    * @date: 2024/3/14
    * @description: 用于设置 Tabbed 选项卡选择以及未被选中时颜色的 UI
    */
    class SettingTabbedPaneUI extends BasicTabbedPaneUI {
        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2d = (Graphics2D) g.create();

            if (isSelected) {
                g2d.setColor(DEEP_BLACK_COLOR); // Set the selected tab color
            } else {
                g2d.setColor(POOL_BLACK_COLOR);
            }

            switch (tabPlacement) {
                case LEFT:
                    g2d.fillRect(x + 1, y + 1, w - 1, h - 3);
                    break;
                case BOTTOM:
                    g2d.fillRect(x + 1, y - 1, w - 3, h - 1);
                    break;
                case RIGHT:
                    g2d.fillRect(x - 1, y + 1, w - 1, h - 3);
                    break;
                case TOP:
                default:
                    g2d.fillRect(x + 1, y + 1, w - 3, h - 1);
                    break;
            }

            g2d.dispose();
        }
    }

    public void manualInitComponents(){
        MainPanel.setBackground(DEEP_BLACK_COLOR);
        TitilePanel.setBackground(DEEP_BLACK_COLOR);
        ImgSettingPanel.setBackground(DEEP_BLACK_COLOR);
        AboutPanel.setBackground(DEEP_BLACK_COLOR);
        HelpPanel.setBackground(DEEP_BLACK_COLOR);
        textArea.setBackground(TEXT_BK_COLOR);
        helpTextArea.setBackground(TEXT_BK_COLOR);
        SettingTbbedPane.setUI(new SettingTabbedPaneUI());

        // 导入本地源按钮
        JButton importLocalBt = RadiusButtonBuilder.createRadiusButton("\u5bfc\u5165\u672c\u5730\u56fe\u7247", EXIT_BUTTON_COLOR,15);
        importLocalBt.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 12));
        importLocalBt.setBounds(265, 130, 120, 35);
        registerButtonTouch(importLocalBt);
        // 导入按钮事件
        importLocalBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose a folder");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int result = fileChooser.showOpenDialog(settingWin);
                // 添加一个对文件夹处理的线程
                if (result == JFileChooser.APPROVE_OPTION) {
                    ImageThreadPoolExecutor.execute(new Thread(()->{
                        ((Index) parentFrame).readLocalImg(fileChooser.getSelectedFile().getAbsolutePath());
                    }));
                    parentFrame.setEnabled(true);
                    settingWin.dispose();
                }
            }
        });
        ImgSettingPanel.add(importLocalBt);


        // 关闭按钮
        JButton closeBt = RadiusButtonBuilder.createRadiusButton("X",EXIT_BUTTON_COLOR,30);
        closeBt.setBounds(370, 5, 25, 25);
        registerButtonTouch(closeBt);
        closeBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingWin.dispatchEvent(new WindowEvent(settingWin,WindowEvent.WINDOW_CLOSING) );
            }
        });
        TitilePanel.add(closeBt);

        // 初始化爬虫列表
        for(int i = 0, j = SearchData.getCrawlerNum(); i<j;i++){
            crawerSourceComBox.addItem(SearchData.getCrawlerSource(i).getSourceName());
        }
        // 更换爬虫
        crawerSourceComBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int selectedIndex = crawerSourceComBox.getSelectedIndex();
                    SearchConfig.setCurrentSourceIndex(selectedIndex);
                    System.out.println("更改爬虫源下标为"+SearchConfig.getCurrentSourceIndex());
                }
            }
        });

    }


    /**
    * @author: auuuu4
    * @date: 2024/3/14
    * @description: 给 frame 添加一个拖动监听，添加后拖动任意位置都可以移动该窗口
    */
    private void registerMouseDraggingFrame(JFrame frame){
        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                isDragging = true;
                preX = e.getX();
                preY = e.getY();
            }
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }
        });
        //时刻更新鼠标位置
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                //修改位置
                if (isDragging) {
                    int left = frame.getLocation().x;
                    int top = frame.getLocation().y;
                    frame.setLocation(left + e.getX() - preX, top + e.getY() - preY);

                }
            }
        });
    }
    /**
    * @author: auuuu4
    * @date: 2024/3/14
    * @description: 给按钮组件注册一个鼠标接触、离开、按下按钮后，按钮变色的监听器
    */
    private void registerButtonTouch(JButton button){
        button.setMargin(new Insets(1, 1, 1, 1));
        MainPanel.add(button);
        RadiusButtonBuilder.registerButtonTouchEven(button,ENTER_BUTTON_COLOR,EXIT_BUTTON_COLOR,PRESS_BUTTON_COLOR);
        button.setFocusPainted(false);
    }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        MainPanel = new JPanel();
        SettingTbbedPane = new JTabbedPane();
        ImgSettingPanel = new JPanel();
        label1 = new JLabel();
        crawerSourceComBox = new JComboBox();
        HelpPanel = new JPanel();
        helpTextArea = new JTextArea();
        AboutPanel = new JPanel();
        textArea = new JTextArea();
        TitilePanel = new JPanel();

        //======== this ========
        setPreferredSize(new Dimension(400, 290));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== MainPanel ========
        {
            MainPanel.setMinimumSize(new Dimension(400, 290));
            MainPanel.setLayout(null);

            //======== SettingTbbedPane ========
            {
                SettingTbbedPane.setPreferredSize(new Dimension(400, 270));
                SettingTbbedPane.setForeground(Color.white);

                //======== ImgSettingPanel ========
                {
                    ImgSettingPanel.setLayout(null);

                    //---- label1 ----
                    label1.setText("\u9009\u62e9\u722c\u53d6\u7684\u6e90\uff08\u9700\u8981\u5148\u5199\u597dProvider\uff09\uff1a");
                    label1.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 12));
                    label1.setForeground(Color.white);
                    ImgSettingPanel.add(label1);
                    label1.setBounds(20, 25, 350, 20);

                    //---- crawerSourceComBox ----
                    crawerSourceComBox.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 12));
                    crawerSourceComBox.setForeground(new Color(0x66ffff));
                    ImgSettingPanel.add(crawerSourceComBox);
                    crawerSourceComBox.setBounds(20, 75, 155, 25);
                }
                SettingTbbedPane.addTab("\u8868\u60c5\u6e90\u8bbe\u7f6e", ImgSettingPanel);

                //======== HelpPanel ========
                {
                    HelpPanel.setLayout(null);

                    //---- helpTextArea ----
                    helpTextArea.setText("+ \u641c\u7d22\u540e\u5982\u679c\u7ed3\u679c\u8f83\u5c11\u53ef\u4ee5\u5c1d\u8bd5\u591a\u641c\u7d22\u51e0\u6b21\n\n+ \u641c\u7d22\u65e0\u7ed3\u679c\u53ef\u80fd\u662f\u6e90\u670d\u52a1\u5668\u6216\u8005\u81ea\u5df1\u7684\u7f51\u7edc\u95ee\u9898\n\n+ \u66f4\u6362\u6e90\u9700\u8981\u7b80\u5355\u7684 JAVA \u57fa\u7840\u77e5\u8bc6\n\n  \u5982\u679c\u6ca1\u6709\u8bf7\u4f7f\u7528\u6807\u6ce8\u9ed8\u8ba4\u6e90\u9879");
                    helpTextArea.setMargin(new Insets(25, 25, 0, 0));
                    helpTextArea.setForeground(new Color(0x00cccc));
                    helpTextArea.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 14));
                    HelpPanel.add(helpTextArea);
                    helpTextArea.setBounds(10, 15, 375, 175);
                }
                SettingTbbedPane.addTab("\u5e2e\u52a9", HelpPanel);

                //======== AboutPanel ========
                {
                    AboutPanel.setLayout(null);

                    //---- textArea ----
                    textArea.setText("\u4f5c\u8005\uff1aauuuu4\n\n\u6b22\u8fce\u53cd\u9988+Q3414608521\n\n\u8be5\u9879\u76ee\u5df2 \u5728GitHub \u5f00\u6e90 \uff1a\n\nhttps://github.com/auuuu4/EmojiSearch\n\nVersion 1.0.0-Bate");
                    textArea.setMargin(new Insets(25, 25, 0, 0));
                    textArea.setForeground(new Color(0x00cccc));
                    textArea.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 14));
                    AboutPanel.add(textArea);
                    textArea.setBounds(10, 15, 375, 175);
                }
                SettingTbbedPane.addTab("\u5173\u4e8e", AboutPanel);
            }
            MainPanel.add(SettingTbbedPane);
            SettingTbbedPane.setBounds(0, 30, 398, 239);

            //======== TitilePanel ========
            {
                TitilePanel.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < TitilePanel.getComponentCount(); i++) {
                        Rectangle bounds = TitilePanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = TitilePanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    TitilePanel.setMinimumSize(preferredSize);
                    TitilePanel.setPreferredSize(preferredSize);
                }
            }
            MainPanel.add(TitilePanel);
            TitilePanel.setBounds(0, 0, 398, 30);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < MainPanel.getComponentCount(); i++) {
                    Rectangle bounds = MainPanel.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = MainPanel.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                MainPanel.setMinimumSize(preferredSize);
                MainPanel.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(MainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel MainPanel;
    private JTabbedPane SettingTbbedPane;
    private JPanel ImgSettingPanel;
    private JLabel label1;
    private JComboBox crawerSourceComBox;
    private JPanel HelpPanel;
    private JTextArea helpTextArea;
    private JPanel AboutPanel;
    private JTextArea textArea;
    private JPanel TitilePanel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
