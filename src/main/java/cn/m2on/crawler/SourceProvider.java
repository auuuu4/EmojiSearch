package cn.m2on.crawler;

/**
 * Created with IntelliJ IDEA.
 * @Author: m2on
 * @Date: 2024/03/13/16:54
 * @Description: 应当实现此接口将表情的 URL 图片上传到 SearchData.imgURLSources
 *
 */
public interface SourceProvider {
    abstract boolean provideSource(String keyWord);

}
