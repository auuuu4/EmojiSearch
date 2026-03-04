package cn.m2on.crawler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 调用该类的 {@link #consume()} 方法，会创建 5 个 ImgCrawler 线程执行
 */
public class ImageThreadPoolExecutor {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private static final AtomicInteger runningWorker = new AtomicInteger(0);

    @Deprecated
    private static CountDownLatch latch;

    @Deprecated
    public static void consumeFinished() {
        latch.countDown();
    }

    @Deprecated
    public static void await() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("等待" + Thread.currentThread().getName() + "线程完成超时");
        }
    }

    public static void consume() {
        latch = new CountDownLatch(5);
        runningWorker.set(5);
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(new ImgCrawler());
        }
        System.out.println("ImageThreadPoolExecutor 爬取图片 URL 结束");
    }

    public static void onWorkerFinished() {
        runningWorker.decrementAndGet();
    }

    public static boolean isIdle() {
        return runningWorker.get() <= 0;
    }

    public static <T extends Runnable> void execute(T thread) {
        threadPoolExecutor.execute(thread);
    }

    public static void reset() {
        threadPoolExecutor.shutdownNow();
        runningWorker.set(0);
        threadPoolExecutor = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }
}
