package cn.m2on.crawler.provider;

import cn.m2on.crawler.SourceProvider;
import cn.m2on.data.SearchData;
import cn.m2on.entity.CrawlerURLSource;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import github.auuuu4.utils.HttpRequestUtil;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * @Author: m2on
 * @Date: 2024/03/13/16:59
 * @Description: ALAPI 的爬虫，此爬虫需要 Token
 */
public class AlapiProvider implements SourceProvider {
    @Override
    public boolean provideSource(String keyWordStr) {
        try{
            CrawlerURLSource source = SearchData.getCrawlerSource(3);
            Map<String ,Object> params = source.getParams();
            params.put(source.getKeyWord(),keyWordStr);
            JSONObject jsonResult = JSONObject.parseObject(HttpRequestUtil.doGetWithParams(source.getUrl(),source.getParams()));
            List<String> imagesUrlList = JSON.parseArray(jsonResult.getString("data"),String.class);
            SearchData.updateImageURL(imagesUrlList);
            return true;
        }catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }


    }


}
