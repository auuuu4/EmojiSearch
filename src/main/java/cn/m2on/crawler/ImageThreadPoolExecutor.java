package cn.m2on.crawler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * @Author: m2on
 * @Date: 2024/03/14/10:37
 * @Description: 调用该类的 {@link #consume()} 方法，会创建 5 个 ImgCrawler 线程执行
 */
public class ImageThreadPoolExecutor {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,10,30, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());

    /**
     * 弃用：需要刷新 UI ，所以不需要等待线程全部完成
     */
    @Deprecated
    private static CountDownLatch latch;

    /**
     * 线程执行完成后调用
     */
    @Deprecated
    public static void consumeFinished(){
        latch.countDown();
    }

    /**
     * 等待线程池爬取任务完成
     */
    @Deprecated
    public static void await(){
        try {
            latch.await();
        }catch (InterruptedException e){
            System.out.println("等待"+Thread.currentThread().getName()+"线程完成超时");
        }

    }

    /**
    * @author: auuuu4
    * @date: 2024/3/14
    * @description: 对所有 URL 进行爬取
    */
    public static void consume(){
        latch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(new ImgCrawler());
        }
        System.out.println("ImageThreadPoolExecutor 爬取图片 URL 结束");
    }

    public static <T extends Runnable> void execute(T thread){
        threadPoolExecutor.execute(thread);
    }

    /**
    * @author: auuuu4
    * @date: 2024/3/14
    * @description: 关闭之前的线程池并且开启一个新的线程池
    */
    public static void reset(){
        threadPoolExecutor.shutdownNow();
        threadPoolExecutor = new ThreadPoolExecutor(5,10,30, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
    }

}
