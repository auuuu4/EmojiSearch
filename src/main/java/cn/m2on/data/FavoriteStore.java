package cn.m2on.data;

import com.alibaba.fastjson2.JSON;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 收藏 URL 存储
 */
public class FavoriteStore {
    private static final Path CONFIG_DIR = Paths.get(System.getProperty("user.home"), ".emoji-search");
    private static final Path FAVORITE_FILE = CONFIG_DIR.resolve("favorites.json");

    private static final Set<String> favorites = new LinkedHashSet<>();

    static {
        load();
    }

    public static synchronized void add(String url) {
        if (url == null || url.trim().isEmpty()) {
            return;
        }
        favorites.add(url.trim());
        save();
    }

    public static synchronized void remove(String url) {
        if (url == null || url.trim().isEmpty()) {
            return;
        }
        favorites.remove(url.trim());
        save();
    }

    public static synchronized boolean contains(String url) {
        if (url == null) {
            return false;
        }
        return favorites.contains(url.trim());
    }

    public static synchronized List<String> list() {
        return new ArrayList<>(favorites);
    }

    private static synchronized void load() {
        if (!Files.exists(FAVORITE_FILE)) {
            return;
        }
        try {
            String content = new String(Files.readAllBytes(FAVORITE_FILE), StandardCharsets.UTF_8);
            List<String> loaded = JSON.parseArray(content, String.class);
            if (loaded != null) {
                favorites.clear();
                for (String item : loaded) {
                    if (item != null && !item.trim().isEmpty()) {
                        favorites.add(item.trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void save() {
        try {
            if (!Files.exists(CONFIG_DIR)) {
                Files.createDirectories(CONFIG_DIR);
            }
            Files.write(FAVORITE_FILE, JSON.toJSONString(favorites).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
