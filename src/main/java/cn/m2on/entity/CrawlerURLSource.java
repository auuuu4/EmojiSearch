package cn.m2on.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

 /**
 * Created with IntelliJ IDEA.
 *
 * @Author: m2on
 * @Date: 2024/03/12/8:35
 * @Description:
 * <pre>
 * Example:
 * 正常搜索方法为 GET 请求的 https://www.pkdoutu.com/search?keyword=abcdefg
 * 那么，应使用 https://www.pkdoutu.com/search 作为 url
 * 使用 GET 方法， keyWord 设置为 keyword
 * 初始化调用应为
 *  new CrawlerURLSource("斗图啦源","https://www.pkdoutu.com/search",Method.GET,"keyword",null)
 *
 * 如果有额外的附加参数 比如需要附带 token ，指定页数,
 * 可以将它们添加到一个 Map< String,Object > params 中,
 * 然后使用下面的代码来初始化一个源
 * new CrawlerURLSource("斗图啦源","https://www.pkdoutu.com/search",Method.GET,"keyword",params)
 *
 * PS: 原本的计划是手动添加源后自动化解析，不过接口不同解析也不同。所以自定义源需要实现 cn.m2on.crawler.SourceProvider 接口来手动解析咯
 * </pre>
 */
@Data
@AllArgsConstructor
public class CrawlerURLSource {

	String sourceName;

    String url;

    @Deprecated
    Method method;

    String keyWord;

    Map<String,Object> params;
}
