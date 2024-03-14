package cn.m2on;

import cn.m2on.config.SearchConfig;
import cn.m2on.ui.Index;
import cn.m2on.ui.Setting;
import cn.m2on.util.ui.UiUtil;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class EmojiSearchApp
{
    public static void main(String[] args) {
        Index indexWin = new Index();
        UiUtil.moveToScreenCenter(indexWin);
        indexWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        indexWin.setVisible(true);
        indexWin.pack();
//        Setting setting = new Setting();
//        UiUtil.moveToScreenCenter(setting);
//        setting.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setting.setVisible(true);
//        setting.pack();
    }
}
