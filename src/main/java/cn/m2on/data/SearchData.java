package cn.m2on.data;

import cn.m2on.crawler.SourceProvider;
import cn.m2on.crawler.provider.DouTuApiProvider;
import cn.m2on.crawler.provider.FavoritesProvider;
import cn.m2on.crawler.provider.GenericApiProvider;
import cn.m2on.crawler.provider.RandomProvider;
import cn.m2on.entity.CrawlerURLSource;
import cn.m2on.entity.CustomApiSource;
import cn.m2on.entity.ImageSource;
import cn.m2on.entity.Method;
import com.alibaba.fastjson2.JSON;
import github.auuuu4.utils.HttpRequestUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 搜索数据中心
 */
public class SearchData {

    public static final String DOUTU_SOURCE_ID = "doutu_default";
    public static final String RANDOM_SOURCE_ID = "random_default";
    public static final String FAVORITES_SOURCE_ID = "favorites_default";

    private static final ArrayBlockingQueue<String> imgURLQueue = new ArrayBlockingQueue<>(300);
    private static final ArrayBlockingQueue<ImageSource> imageQueue = new ArrayBlockingQueue<>(300);

    private static final List<ProviderEntry> providers = new ArrayList<>();
    private static final Map<String, CrawlerURLSource> crawlerSourceMap = new LinkedHashMap<>();
    private static final List<CustomApiSource> customApiSources = new ArrayList<>();

    private static final Path CONFIG_DIR = Paths.get(System.getProperty("user.home"), ".emoji-search");
    private static final Path CUSTOM_SOURCE_FILE = CONFIG_DIR.resolve("custom-sources.json");

    static {
        // 初始化工具类，减少首次搜索耗费的时间
        try {
            System.out.println(HttpRequestUtil.doGet("127.0.0.1"));
        } catch (RuntimeException ignore) {
        }
        initDefaultProviders();
        loadCustomSources();
    }

    private static void initDefaultProviders() {
        HashMap<String, Object> doutuParams = new HashMap<>();
        CrawlerURLSource doutu = new CrawlerURLSource("斗图api(默认)", "https://doutu.lccyy.com/doutu/items", Method.GET, "keyword", doutuParams);
        registerProvider(DOUTU_SOURCE_ID, doutu, new DouTuApiProvider());

        HashMap<String, Object> randomParams = new HashMap<>();
        randomParams.put("return", "json");
        randomParams.put("num", 100);
        CrawlerURLSource random = new CrawlerURLSource("随机图片源", "https://img.moehu.org/pic.php", Method.GET, "keyword", randomParams);
        registerProvider(RANDOM_SOURCE_ID, random, new RandomProvider());

        CrawlerURLSource favorites = new CrawlerURLSource("收藏源", "favorite://local", Method.GET, "keyword", new HashMap<String, Object>());
        registerProvider(FAVORITES_SOURCE_ID, favorites, new FavoritesProvider());
    }

    private static void registerProvider(String sourceId, CrawlerURLSource source, SourceProvider provider) {
        crawlerSourceMap.put(sourceId, source);
        providers.add(new ProviderEntry(sourceId, provider));
    }

    public static synchronized void addCustomApiSource(CustomApiSource custom) {
        if (custom.getSourceId() == null || custom.getSourceId().trim().isEmpty()) {
            custom.setSourceId("custom_" + System.currentTimeMillis());
        }

        if (crawlerSourceMap.containsKey(custom.getSourceId())) {
            throw new IllegalArgumentException("sourceId 已存在: " + custom.getSourceId());
        }
        for (CustomApiSource source : customApiSources) {
            if (source.getSourceName().equals(custom.getSourceName())) {
                throw new IllegalArgumentException("sourceName 已存在: " + custom.getSourceName());
            }
        }

        HashMap<String, Object> params = new HashMap<>();
        CrawlerURLSource source = new CrawlerURLSource(
                custom.getSourceName(),
                custom.getUrl(),
                Method.GET,
                custom.getKeywordParam(),
                params
        );
        registerProvider(
                custom.getSourceId(),
                source,
                new GenericApiProvider(custom.getSourceId(), custom.getListField(), custom.getImageField())
        );
        customApiSources.add(custom);
        saveCustomSources();
    }

    private static synchronized void loadCustomSources() {
        if (!Files.exists(CUSTOM_SOURCE_FILE)) {
            return;
        }
        try {
            String content = new String(Files.readAllBytes(CUSTOM_SOURCE_FILE), StandardCharsets.UTF_8);
            List<CustomApiSource> loaded = JSON.parseArray(content, CustomApiSource.class);
            if (loaded == null) {
                return;
            }
            for (CustomApiSource custom : loaded) {
                if (custom.getSourceName() == null || custom.getUrl() == null || custom.getKeywordParam() == null
                        || custom.getListField() == null || custom.getImageField() == null) {
                    continue;
                }
                addCustomApiSourceWithoutSave(custom);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addCustomApiSourceWithoutSave(CustomApiSource custom) {
        HashMap<String, Object> params = new HashMap<>();
        CrawlerURLSource source = new CrawlerURLSource(
                custom.getSourceName(),
                custom.getUrl(),
                Method.GET,
                custom.getKeywordParam(),
                params
        );
        registerProvider(
                custom.getSourceId(),
                source,
                new GenericApiProvider(custom.getSourceId(), custom.getListField(), custom.getImageField())
        );
        customApiSources.add(custom);
    }

    private static synchronized void saveCustomSources() {
        try {
            if (!Files.exists(CONFIG_DIR)) {
                Files.createDirectories(CONFIG_DIR);
            }
            Files.write(CUSTOM_SOURCE_FILE, JSON.toJSONString(customApiSources).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SourceProvider getCrawler(int index) {
        return providers.get(index).provider;
    }

    public static int getCrawlerNum() {
        return providers.size();
    }

    public static CrawlerURLSource getCrawlerSource(int index) {
        return crawlerSourceMap.get(providers.get(index).sourceId);
    }

    public static CrawlerURLSource getCrawlerSourceById(String sourceId) {
        return crawlerSourceMap.get(sourceId);
    }

    public static void updateImageURL(List<String> sourceList) {
        for (String url : sourceList) {
            try {
                imgURLQueue.put(url);
            } catch (InterruptedException e) {
                System.out.println("将" + url + "放入 urlQueue 时出错");
            }
        }
    }

    public static void updateImageSource(List<ImageSource> sources) {
        imageQueue.clear();
        for (ImageSource source : sources) {
            try {
                imageQueue.put(source);
            } catch (InterruptedException e) {
                System.out.println("将" + source.getImgUrl() + "放入 imageQueue 时出错");
            }
        }
    }

    public static boolean isURLSourceEmpty() {
        return imgURLQueue.isEmpty();
    }

    public static boolean isImageSourceEmpty() {
        return imageQueue.isEmpty();
    }

    public static ImageSource takeImageSource() throws InterruptedException {
        return imageQueue.take();
    }

    public static String takenImageURL() throws InterruptedException {
        return imgURLQueue.take();
    }

    public static void addImageSource(ImageSource source) {
        try {
            imageQueue.put(source);
        } catch (InterruptedException e) {
            System.out.println("将" + source.getImgUrl() + "放入 imgQueue 时出错");
        }
    }

    public static void clearUrlQueue() {
        imgURLQueue.clear();
    }

    public static void clearImageQueue() {
        imageQueue.clear();
    }


    public static synchronized List<CustomApiSource> getCustomApiSourcesSnapshot() {
        return new ArrayList<>(customApiSources);
    }

    public static synchronized boolean removeCustomApiSourceById(String sourceId) {
        if (sourceId == null || sourceId.trim().isEmpty()) {
            return false;
        }
        boolean removedMeta = customApiSources.removeIf(s -> sourceId.equals(s.getSourceId()));
        if (!removedMeta) {
            return false;
        }

        crawlerSourceMap.remove(sourceId);
        providers.removeIf(entry -> sourceId.equals(entry.sourceId));
        saveCustomSources();
        return true;
    }

    public static synchronized int findSourceIndexById(String sourceId) {
        for (int i = 0; i < providers.size(); i++) {
            if (sourceId.equals(providers.get(i).sourceId)) {
                return i;
            }
        }
        return -1;
    }

    public static synchronized String getSourceIdByIndex(int index) {
        if (index < 0 || index >= providers.size()) {
            return null;
        }
        return providers.get(index).sourceId;
    }


    private static class ProviderEntry {
        private final String sourceId;
        private final SourceProvider provider;

        private ProviderEntry(String sourceId, SourceProvider provider) {
            this.sourceId = sourceId;
            this.provider = provider;
        }
    }
}
