package cn.m2on.util.ui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: m2on
 * @Date: 2024/03/09/19:07
 * @Description:
 */
public class UiUtil {

    /**
    * @author: auuuu4
    * @date: 2024/3/12
    * @param file : 图标文件
    * @param com : JButton 按钮
    * @return : void
    * @description: 给按钮设置一个与按钮大小一样的图标
    */
    public static void setIcon(String file,JButton com)
    {
        ImageIcon ico=new ImageIcon(file);
        Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);
        ico=new ImageIcon(temp);
        com.setIcon(ico);
    }

    /**
    * @Author: m2on
    * @Date: 2024/3/9
    * @Description: 设置全局字体
    * @Param font : 字体
    * @Return : void
    */
    public static void initGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
    /**
    * @Author: m2on
    * @Date: 2024/3/9
    * @Description: 将窗口移动到屏幕中心
    * @Param window : 需要移动的窗口
    * @Return : void
    */
    public static void moveToScreenCenter(Window window){
        Point pointScreenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        window.setLocation(pointScreenCenter.x-window.getPreferredSize().width/2, pointScreenCenter.y-window.getPreferredSize().height/2);
    }


    /**
     * @date: 2024/3/13
     * @description: 用于剪切图片到剪切板的图片工具类
     */
    public static class ImageSelection implements Transferable {
        private Image image;

        public ImageSelection(Image image) {
            this.image = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {

            return DataFlavor.imageFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (DataFlavor.imageFlavor.equals(flavor)) {
                return image;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }
    public static void addTouchChangeColor(JComponent component,Color entCor,Color extCor){
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {  //鼠标移上去
                component.setBackground(entCor);
            }
        });
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {  //鼠标离开
                component.setBackground(extCor);
            }
        });
    }
}
