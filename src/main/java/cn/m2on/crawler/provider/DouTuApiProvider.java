package cn.m2on.crawler.provider;

import cn.m2on.crawler.SourceProvider;
import cn.m2on.data.SearchData;
import cn.m2on.entity.CrawlerURLSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.auuuu4.utils.HttpRequestUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * @Author: auuuu4
 * @Date: 2024/03/14/
 * @Description: 斗图Api 的爬虫
 */
public class DouTuApiProvider implements SourceProvider {
    @Override
    public boolean provideSource(String keyWordStr) {
        try {ArrayList<String> imagesUrlList = new ArrayList<>();
            CrawlerURLSource source = SearchData.getCrawlerSource(0);
            Map<String ,Object> params = source.getParams();
            // 可选参数，第几页，每页表情数量
            params.put("pageNum",1);
            params.put("pageSize",100);

            params.put(source.getKeyWord(),keyWordStr);
            JSONObject jsonResult = JSONObject.parseObject(HttpRequestUtil.doGetWithParams(source.getUrl(),source.getParams()));
            JSONArray jsonArray = jsonResult.getJSONArray("items");
            for(int i=0;i<jsonArray.size();i++){
                imagesUrlList.add(jsonArray.getJSONObject(i).getString("url"));
            }
            SearchData.updateImageURL(imagesUrlList);
            return true;
        }catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }

    }


}
