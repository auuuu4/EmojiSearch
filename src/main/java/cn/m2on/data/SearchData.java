package cn.m2on.data;

import cn.m2on.crawler.SourceProvider;
import cn.m2on.crawler.provider.AlapiProvider;
import cn.m2on.crawler.provider.DouTuApiProvider;
import cn.m2on.crawler.provider.RandomProvider;
import cn.m2on.entity.CrawlerURLSource;
import cn.m2on.entity.ImageSource;
import cn.m2on.entity.Method;
import github.auuuu4.utils.HttpRequestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * @Author: m2on
 * @Date: 2024/03/14/9:39
 * @Description:
 */
public class SearchData {
    /**
     * 爬虫源列表
     */
    private static final ArrayList<CrawlerURLSource> crawlerSourcesArray = new ArrayList<>();

    /**
     * 等待爬取的图片 Url
     */
    private static final ArrayBlockingQueue<String> imgURLQueue = new ArrayBlockingQueue<>(100);

    /**
     * 爬取完的图片资源
     */
    private static final ArrayBlockingQueue<ImageSource> imageQueue = new ArrayBlockingQueue<>(100);

    /**
     * 爬虫列表
     */
    private static final ArrayList<SourceProvider> crawlers = new ArrayList<>();

    //初始化工具类，减少首次搜索耗费的时间
    static {
        System.out.println(HttpRequestUtil.doGet("127.0.0.1"));
    }

    // 添加初始爬虫
    static {
        crawlers.add(new DouTuApiProvider());
        crawlers.add(new RandomProvider());
//        crawlers.add(new AlapiProvider());

    }


    // 添加初始源
    static {
        // DouTuApi
        HashMap<String,Object> params = new HashMap<>();
        crawlerSourcesArray.add(new CrawlerURLSource("斗图api(默认)","https://doutu.lccyy.com/doutu/items",Method.GET,"keyword",params));

        // RandomApi
        params.clear();
        params.put("return","json");
        params.put("num",100);
        crawlerSourcesArray.add(new CrawlerURLSource("随机图片源","https://img.moehu.org/pic.php",Method.GET,"keyword",params));

        // AlApi
//        params.put("token","xxxxxx");
//        crawlerSourcesArray.add(new CrawlerURLSource("ALAPI源","https://v2.alapi.cn/api/doutu", Method.GET,"keyword",params));
//        params.clear();

    }

    public static SourceProvider getCrawler(int index){
        return crawlers.get(index);
    }

    public static void updateImageURL(List<String> sourceList){
        for (String url: sourceList) {
            try {
                imgURLQueue.put(url);
            }catch (InterruptedException e){
                System.out.println("将"+url+"放入 urlQueue 时出错");
            }

        }

    }
    public static void updateImageSource(List<ImageSource> sources){
        imageQueue.clear();
        for (ImageSource source: sources) {
            try {
                imageQueue.put(source);
            }catch (InterruptedException e){
                System.out.println("将"+source.getImgUrl()+"放入 imageQueue 时出错");
            }

        }
    }

    public static boolean isURLSourceEmpty(){
        return imgURLQueue.isEmpty();
    }

    public static boolean isImageSourceEmpty(){
        return imageQueue.isEmpty();
    }

    public static CrawlerURLSource getCrawlerSource(int index){
        return crawlerSourcesArray.get(index);
    }

    public static int getCrawlerNum(){
        return crawlerSourcesArray.size();
    }
    public static int getCrawlerSourceSize(){return crawlerSourcesArray.size();}

    public static ImageSource takeImageSource() throws InterruptedException {
        return imageQueue.take();
    }

    public static String takenImageURL() throws InterruptedException {
        return imgURLQueue.take();
    }

    public static void addImageSource(ImageSource source){
        try {
            imageQueue.put(source);
        }catch (InterruptedException e){
            System.out.println("将"+source.getImgUrl()+"放入 imgQueue 时出错");
        }

    }
    public static void clearUrlQueue(){
        imgURLQueue.clear();
    }

}
