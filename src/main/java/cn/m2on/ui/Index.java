/*
 * Created by JFormDesigner on Tue Mar 12 21:26:44 HKT 2024
 */

package cn.m2on.ui;

import cn.m2on.config.SearchConfig;
import cn.m2on.crawler.ImageThreadPoolExecutor;
import cn.m2on.crawler.SourceProvider;
import cn.m2on.crawler.provider.AlapiProvider;
import cn.m2on.crawler.provider.RandomProvider;
import cn.m2on.data.SearchData;
import cn.m2on.entity.ImageSource;
import cn.m2on.util.ui.RadiusButtonBuilder;
import cn.m2on.util.ui.UiUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;


public class Index extends JFrame {
    private final JFrame indexWin = this;
    public static final Color ENTER_BUTTON_COLOR = Color.LIGHT_GRAY;

    public static final Color PRESS_BUTTON_COLOR = new Color(65, 68, 70);

    public static final Color EXIT_BUTTON_COLOR = Color.GRAY;

    public static final Color DEEP_BLACK_COLOR = new Color(24, 24, 24, 255);
    public static final Color POOL_BLACK_COLOR = new Color(94, 89, 89, 255);
    public static final Color IMG_PANEL_BLACK_COLOR = new Color(183, 183, 183, 255);

    private int preX,preY;
    private boolean isDragging;

    private String searchContent;


    public Index() {
        setUndecorated(true);
        initComponents();
        manualInitComponents();

        getContentPane().setBackground(new Color(33,37,43));
        setPreferredSize(new Dimension(530,360));
        registerMouseDraggingFrame(this);
    }
    /**
    * @author: auuuu4
    * @date: 2024/3/13
    * @return : java.lang.String
    * @description: 获取搜索内容
    */
    public String getSearchContent(){
        return searchContent;
    }




    /**
    * @author: auuuu4
    * @date: 2024/3/13
    * @return : null
    * @description: 插入解析后的表情列表
    */
    public void insertImages(){
        while(SearchData.isImageSourceEmpty()){
            try {
                ImageSource source = SearchData.takeImageSource();
                System.out.println("准备插入"+source.getImgUrl());
                try {
                    insertImg(source.getImage());
                }catch (NullPointerException e){
                    System.out.println("插入"+source.getImgUrl()+"失败");
                }
                System.out.println("已插入"+source.getImgUrl());
            }catch (InterruptedException e){
                System.out.println("添加表情包资源失败");
            }

        }
    }
    /**
    * @author: auuuu4
    * @date: 2024/3/13
    * @param image : 需要插入的图片
    * @return : void
    * @description: 增添表情到表情框
    */
    private void insertImg(Image image){
        JButton newBt = new JButton(new ImageIcon(image));
        newBt.setPreferredSize(new Dimension(155,155));
        newBt.setMargin(new Insets(0,0,0,0));

        // 图标自适应按钮大小
        ImageIcon newIco = new ImageIcon(image.getScaledInstance(newBt.getPreferredSize().width,newBt.getPreferredSize().height,Image.SCALE_SMOOTH));

        newBt.setIcon(newIco);
        newBt.setOpaque(false);
        newBt.setContentAreaFilled(false);
        newBt.setBorderPainted(false);


        // 监听点击事件，将图片剪切到剪切版
        newBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Icon icon = newBt.getIcon();
                if (icon instanceof ImageIcon) {
                    BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = image.createGraphics();
                    icon.paintIcon(newBt, g2d, 0, 0);
                    g2d.dispose();
                    UiUtil.ImageSelection imageSelection = new UiUtil.ImageSelection(image);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imageSelection, null);
                }
            }
        });
        ImagePanel.add(newBt);
    }

    /**
    * @author: auuuu4
    * @date: 2024/3/13
    * @return : void
    * @description: 清除表情框内全部表情
    */
    public void clearAllImg(){
        ImagePanel.removeAll();
    }


    /**
    * @author: auuuu4
    * @date: 2024/3/13

    * @return : void
    * @description:
    */
    private void initUnVisibleScroll(){
        ImageScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ImageScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ImageScrollPanel.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
        ImageScrollPanel.getVerticalScrollBar().setUnitIncrement(20);
    }



    /**
     * test
     */
    public void test(){
        RandomProvider crawler = new RandomProvider();
//        crawler.provideSource();
        CountDownLatch latch = new CountDownLatch(20);
        ImageThreadPoolExecutor.consume();
        ImageThreadPoolExecutor.await();
        insertImages();

    }
    /**
     * @param frame : 需要添加拖动监听的窗口
     * @return : void
     * @author: auuuu4
     * @date: 2024/3/12
     * @description: 给 frame 添加一个拖动监听，添加后拖动任意位置都可以移动该窗口
     */
    private void registerMouseDraggingFrame(JFrame frame){
        frame.addMouseListener(new MouseAdapter() {    //给JFrame窗体添加一个鼠标监听
            public void mousePressed(MouseEvent e) {     //鼠标点击时记录一下初始位置
                isDragging = true;
                preX = e.getX();
                preY = e.getY();
            }
            public void mouseReleased(MouseEvent e) {  //鼠标松开时
                isDragging = false;
            }
        });
        //时刻更新鼠标位置
        frame.addMouseMotionListener(new MouseMotionAdapter() { //添加指定的鼠标移动侦听器，以接收发自此组件的鼠标移动事件。如果侦听器 l 为 null，则不会抛                                                         出异常并且不执行动作。
            public void mouseDragged(MouseEvent e) {
                //修改位置
                if (isDragging) {                                //只要鼠标是点击的（isDragging），就时刻更改窗体的位置
                    int left = frame.getLocation().x;
                    int top = frame.getLocation().y;
                    frame.setLocation(left + e.getX() - preX, top + e.getY() - preY);

                }
            }
        });
    }
    /**
     * @param button : 要注册的按钮组件
     * @return : void
     * @author: auuuu4
     * @date: 2024/3/13
     * @description: 给按钮组件注册一个鼠标接触、离开、按下按钮后，按钮变色的监听器
     */
    private void registerButtonTouch(JButton button){
        button.setMargin(new Insets(1, 1, 1, 1));
        MainPanel.add(button);
        RadiusButtonBuilder.registerButtonTouchEven(button,ENTER_BUTTON_COLOR,EXIT_BUTTON_COLOR,PRESS_BUTTON_COLOR);
        button.setFocusPainted(false);
    }
    /**
    * @author: auuuu4
    * @date: 2024/3/14
    * @description: 导入本地图片为表情包
    */
    public void readLocalImg(String path){
        ImagePanel.removeAll();
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            ArrayList<ImageSource> sources = new ArrayList<>();
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        // Check if the file is an image file
                        if (isImageFile(file)) {
                            // Load the image and add it to the ImagePanel
                            Image img = ImageIO.read(file);
                            sources.add(new ImageSource(file.getAbsolutePath(),img));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            SearchData.updateImageSource(sources);
            ImagePanel.removeAll();
            insertImages();
        }
    }
    /**
    * @author: auuuu4
    * @date: 2024/3/14
    * @description: 判断一个文件是否是图片 jpg jpeg png gif
    */
    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif");
    }
    Thread imageUpdateThread;

    /**
     * @return : void
     * @author: auuuu4
     * @date: 2024/3/13
     * @description: 手动初始化组件
     */
    private void manualInitComponents(){


        // 设置各部分背景色
        MainPanel.setBackground(DEEP_BLACK_COLOR);
        searchTextField.setBackground(DEEP_BLACK_COLOR);
        ImageScrollPanel.setBackground(DEEP_BLACK_COLOR);
        ImagePanel.setBackground(IMG_PANEL_BLACK_COLOR);

        // 设置背景透明
        ImageScrollPanel.setOpaque(false);
        ImageScrollPanel.getViewport().setOpaque(false);



        //---- settingBt ----
        JButton settingBt = RadiusButtonBuilder.createRadiusButton("S",EXIT_BUTTON_COLOR,30);
        settingBt.setBounds(435, 5, 25, 25);
        registerButtonTouch(settingBt);
        settingBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllImg();
                Setting setting = new Setting(indexWin);
                indexWin.setEnabled(false);
                setting.setVisible(true);
                setting.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        indexWin.setEnabled(true);
                    }
                });
            }
        });
        //---- settingBt ----

        //---- minBt ----
        JButton minBt = RadiusButtonBuilder.createRadiusButton("\u2014", EXIT_BUTTON_COLOR,30);
        minBt.setBounds(465, 5, 25, 25);

        registerButtonTouch(minBt);
        minBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TrayIcon trayIcon = new TrayIcon(ImageIO.read(new File("F:\\JavaDemo\\EmojiSearch\\src\\main\\resources\\img\\tray.png")));
                    SystemTray systemTray = SystemTray.getSystemTray();


                    trayIcon.setImageAutoSize(true);
                    trayIcon.setToolTip("点击显示表情包搜索工具");
                    try {

                        systemTray.add(trayIcon);
                    } catch (AWTException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    indexWin.setVisible(false);
                    trayIcon.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int clt = e.getClickCount();
                            if (clt == 1) {
                                indexWin.setExtendedState(NORMAL);
                            }
                            systemTray.remove(trayIcon);
                            indexWin.setVisible(true);
                            indexWin.setAlwaysOnTop(true);
                            indexWin.setAlwaysOnTop(false); // 设置窗口不再保持在顶部
                        }
                    });

                }catch (IOException ie){
                    System.out.println("托盘添加失败");
                    System.out.println(System.getProperty("user.dir"));
                    JOptionPane.showMessageDialog(indexWin, "托盘添加失败，已最小化窗口", "提示",JOptionPane.WARNING_MESSAGE);
                    indexWin.setVisible(true);
                    indexWin.setExtendedState(JFrame.ICONIFIED);

                }




            }
        });


        //---- minBt ----


        //---- closeBt ----
        JButton closeBt = RadiusButtonBuilder.createRadiusButton("X",EXIT_BUTTON_COLOR,30);
        closeBt.setBounds(495, 5, 25, 25);
        registerButtonTouch(closeBt);
        closeBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indexWin.dispatchEvent(new WindowEvent(indexWin,WindowEvent.WINDOW_CLOSING) );
            }
        });
        //---- closeBt ----

        // 创建一个不可见的 scroll
        initUnVisibleScroll();

        // 创建一个不断消费待添加表情的线程，没有表情时阻塞
        imageUpdateThread = new Thread(()->{
            while(true){
                try {
                    ImageSource source = SearchData.takeImageSource();
                    System.out.println("准备插入"+source.getImgUrl());
                    try {
                        insertImg(source.getImage());
                        indexWin.pack();
                        System.out.println("已插入"+source.getImgUrl());
                    }catch (NullPointerException ne){
                        System.out.println("插入"+source.getImgUrl()+"失败");
                    }

                }catch (InterruptedException ie){
                    System.out.println("添加表情包资源失败");
                }


            }
        });
        imageUpdateThread.start();

        // 回车确认搜索
        searchTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                SearchData.clearUrlQueue();
                ImageThreadPoolExecutor.reset();
                ImageThreadPoolExecutor.execute(new Thread(()->{
                    searchContent = searchTextField.getText();
                    System.out.println("改变输入："+searchContent);
                    ImagePanel.removeAll();
                    // 开始搜索表情
                    SourceProvider provider = SearchData.getCrawler(SearchConfig.getCurrentSourceIndex());
                    boolean rst = provider.provideSource(searchContent);
                    if(rst){
//                        JOptionPane.showMessageDialog(indexWin, "搜索完成", "提示",JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(indexWin, "该表情源搜索出错，请更换其他源", "错误",JOptionPane.ERROR_MESSAGE);
                    }
                    ImageThreadPoolExecutor.consume();
                }));
            }
        });
    }








    /**
     * @return : void
     * @author: auuuu4
     * @date: 2024/3/13
     * @description: JFromDesigner 自动生成
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        MainPanel = new JPanel();
        ImageScrollPanel = new JScrollPane();
        ImagePanel = new JPanel();
        tipLabel = new JLabel();
        searchTextField = new JTextField();

        //======== this ========
        setBackground(UIManager.getColor("Button.background"));
        setAutoRequestFocus(false);
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== MainPanel ========
        {
            MainPanel.setBackground(UIManager.getColor("Button.background"));
            MainPanel.setLayout(null);

            //======== ImagePanel ========
            {

                //======== ImagePanel ========
                {
                    ImagePanel.setForeground(UIManager.getColor("Button.background"));
                    ImagePanel.setBackground(UIManager.getColor("Button.background"));
                    ImagePanel.setLayout(new GridLayout(0, 3, 5, 5));
                }
                ImageScrollPanel.setViewportView(ImagePanel);
            }
            MainPanel.add(ImageScrollPanel);
            ImageScrollPanel.setBounds(10, 65, 500, 275);

            //---- tipLabel ----
            tipLabel.setText("\u8f93\u5165\u9700\u8981\u641c\u7d22\u7684\u8868\u60c5\u6807\u7b7e\u540e\u6309\u56de\u8f66:");
            tipLabel.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 12));
            tipLabel.setHorizontalAlignment(SwingConstants.CENTER);
            tipLabel.setForeground(Color.white);
            MainPanel.add(tipLabel);
            tipLabel.setBounds(10, 25, 210, 25);

            //---- searchTextField ----
            searchTextField.setForeground(Color.white);
            searchTextField.setCaretColor(new Color(0x99ffff));
            searchTextField.setSelectionColor(Color.white);
            MainPanel.add(searchTextField);
            searchTextField.setBounds(220, 20, 205, 30);
        }
        contentPane.add(MainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel MainPanel;
    private JScrollPane ImageScrollPanel;
    private JPanel ImagePanel;
    private JLabel tipLabel;
    private JTextField searchTextField;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

}
