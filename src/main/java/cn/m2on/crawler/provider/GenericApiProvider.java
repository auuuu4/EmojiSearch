package cn.m2on.crawler.provider;

import cn.m2on.crawler.SourceProvider;
import cn.m2on.data.SearchData;
import cn.m2on.entity.CrawlerURLSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.auuuu4.utils.HttpRequestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用 JSON API Provider：
 * 1. 请求后获取指定 listField 数组
 * 2. 从数组元素对象读取 imageField 作为图片 URL
 */
public class GenericApiProvider implements SourceProvider {
    private final String sourceId;
    private final String listField;
    private final String imageField;

    public GenericApiProvider(String sourceId, String listField, String imageField) {
        this.sourceId = sourceId;
        this.listField = listField;
        this.imageField = imageField;
    }

    @Override
    public boolean provideSource(String keyWord) {
        try {
            CrawlerURLSource source = SearchData.getCrawlerSourceById(sourceId);
            if (source == null) {
                return false;
            }
            Map<String, Object> params = new HashMap<>(source.getParams());
            params.put(source.getKeyWord(), keyWord);

            JSONObject jsonResult = JSONObject.parseObject(HttpRequestUtil.doGetWithParams(source.getUrl(), params));
            JSONArray items = jsonResult.getJSONArray(listField);
            if (items == null) {
                return false;
            }

            ArrayList<String> imageUrls = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item != null) {
                    String url = item.getString(imageField);
                    if (url != null && !url.trim().isEmpty()) {
                        imageUrls.add(url);
                    }
                }
            }
            SearchData.updateImageURL(imageUrls);
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }
}
