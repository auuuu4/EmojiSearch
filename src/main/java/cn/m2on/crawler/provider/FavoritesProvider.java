package cn.m2on.crawler.provider;

import cn.m2on.crawler.SourceProvider;
import cn.m2on.data.FavoriteStore;
import cn.m2on.data.SearchData;

/**
 * 收藏源 Provider
 */
public class FavoritesProvider implements SourceProvider {
    @Override
    public boolean provideSource(String keyWord) {
        SearchData.updateImageURL(FavoriteStore.list());
        return true;
    }
}
