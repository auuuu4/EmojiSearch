package cn.m2on.crawler;

import cn.m2on.data.SearchData;
import cn.m2on.entity.ImageSource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * @Author: m2on
 * @Date: 2024/03/14/9:35
 * @Description: 消费 SearchData.imgURLSources 中的资源，并将爬取到的图片添加到 SearchData.imageSources
 */
public class ImgCrawler implements SourceCrawler{
    @Override
    public void run() {
        try {
            while(!SearchData.isURLSourceEmpty()){
                String urlStr = SearchData.takenImageURL();
                URL url = new URL(urlStr);
                // 打开连接
                URLConnection connection = url.openConnection();
                // 设置连接超时时间为1秒
                connection.setConnectTimeout(1000);
                // 读取图片数据
                Image image = ImageIO.read(connection.getInputStream());
//                Image image = ImageIO.read(url.openStream());
                ImageSource newSource = new ImageSource(urlStr,image);
                SearchData.addImageSource(newSource);
                System.out.println("线程"+Thread.currentThread().getName()+"爬取"+newSource.getImgUrl()+"完成");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("线程"+Thread.currentThread().getName()+"获取图片资源失败");
        }finally {
            System.out.println("线程"+Thread.currentThread().getName()+"释放了");
        }
    }
}
