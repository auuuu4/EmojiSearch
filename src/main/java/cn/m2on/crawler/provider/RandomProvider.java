package cn.m2on.crawler.provider;

import cn.m2on.crawler.SourceProvider;
import cn.m2on.data.SearchData;
import cn.m2on.entity.CrawlerURLSource;
import cn.m2on.entity.ImageSource;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import github.auuuu4.utils.HttpRequestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * @Author: m2on
 * @Date: 2024/03/13/16:59
 * @Description: 随机源的爬虫
 */
public class RandomProvider implements SourceProvider {
    public static final ArrayList<ImageSource> imageSources = new ArrayList<>();
    @Override
    public void provideSource(String keyWord) {
        CrawlerURLSource source = SearchData.getCrawlerSource(1);
        Map<String ,Object> params = source.getParams();
        JSONObject jsonResult = JSONObject.parseObject(HttpRequestUtil.doGetWithParams(source.getUrl(),source.getParams()));
        System.out.println(jsonResult);
        List<String> imagesUrlList = JSON.parseArray(jsonResult.getString("pic"),String.class);
        SearchData.updateImageURL(imagesUrlList);

    }



}
