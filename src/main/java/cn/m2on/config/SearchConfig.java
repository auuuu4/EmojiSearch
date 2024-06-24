package cn.m2on.config;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: m2on
 * @Date: 2024/03/09/15:33
 * @Description: 搜索配置
 */
public class SearchConfig {
    /**
     * 显示的图片列数
     */
    private static int everyColumnsImgNum = 3;
    /**
     * 当前使用的爬虫源下标
     */
    private static int currentSourceIndex = 0;

    public static void setCurrentSourceIndex(int index){
        currentSourceIndex = index;
    }
    public static int getCurrentSourceIndex(){
        return currentSourceIndex;
    }

}
