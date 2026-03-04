package cn.m2on.crawler.provider;

import cn.m2on.crawler.SourceProvider;
import cn.m2on.data.SearchData;
import cn.m2on.entity.CrawlerURLSource;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import github.auuuu4.utils.HttpRequestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 随机源爬虫
 */
public class RandomProvider implements SourceProvider {

    @Override
    public boolean provideSource(String keyWord) {
        try {
            CrawlerURLSource source = SearchData.getCrawlerSourceById(SearchData.RANDOM_SOURCE_ID);
            Map<String, Object> params = new HashMap<>(source.getParams());
            JSONObject jsonResult = JSONObject.parseObject(HttpRequestUtil.doGetWithParams(source.getUrl(), params));
            List<String> imagesUrlList = JSON.parseArray(jsonResult.getString("pic"), String.class);
            SearchData.updateImageURL(imagesUrlList);
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }
}
