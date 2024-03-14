package cn.m2on.crawler;

/**
 * Created with IntelliJ IDEA.
 * @Author: m2on
 * @Date: 2024/03/14/9:33
 * @Description:
 */
public interface SourceCrawler extends Runnable{
    /**
    * @date: 2024/3/14
    * @description: 实现这个接口来对单次图片进行爬取，并将图片的 URL 存储到 SearchConfig.crawlerSources 中
    */
    void run() ;
}
