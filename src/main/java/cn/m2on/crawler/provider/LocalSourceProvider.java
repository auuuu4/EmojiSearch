package cn.m2on.crawler.provider;

import cn.m2on.crawler.SourceProvider;
import cn.m2on.data.SearchData;
import cn.m2on.entity.ImageSource;
import cn.m2on.entity.LocalImageSource;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * @Author: auuuu4
 * @Date: 2024/06/24/15:35
 * @Description: 需要调整本地的使用方式
 */public class LocalSourceProvider implements SourceProvider {
    @Override
    public boolean provideSource(String path) {
        try {
            File file = new File(path);
            if(!file.isDirectory()){
                throw new IllegalArgumentException("path is not a directory");
            }else{
                ArrayList<String> imagesUrlList = new ArrayList<>();
                for(File f : file.listFiles()){
                    // 判断 f 是否为图片
                    boolean fImg = f.isFile() && (f.getName().endsWith(".jpg") || f.getName().endsWith(".png") || f.getName().endsWith(".jpeg"));
                    if(fImg){
                        imagesUrlList.add(f.getAbsolutePath());
                    }
                }
                SearchData.updateImageURL(imagesUrlList);
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;


        }
    }
}
