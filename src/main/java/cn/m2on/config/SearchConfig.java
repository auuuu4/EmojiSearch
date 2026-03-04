package cn.m2on.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索配置
 */
public class SearchConfig {
    private static int everyColumnsImgNum = 3;
    private static int currentSourceIndex = 0;
    private static final List<String> recentKeywords = new ArrayList<>();

    private static final Path CONFIG_DIR = Paths.get(System.getProperty("user.home"), ".emoji-search");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("app.properties");

    static {
        load();
    }

    public static void setCurrentSourceIndex(int index) {
        currentSourceIndex = index;
        save();
    }

    public static int getCurrentSourceIndex() {
        return currentSourceIndex;
    }


    public static synchronized void addRecentKeyword(String keyword) {
        if (keyword == null) {
            return;
        }
        String normalized = keyword.trim();
        if (normalized.isEmpty()) {
            return;
        }
        recentKeywords.remove(normalized);
        recentKeywords.add(0, normalized);
        while (recentKeywords.size() > 8) {
            recentKeywords.remove(recentKeywords.size() - 1);
        }
        save();
    }

    public static synchronized List<String> getRecentKeywords() {
        return new ArrayList<>(recentKeywords);
    }


    private static void save() {
        try {
            if (!Files.exists(CONFIG_DIR)) {
                Files.createDirectories(CONFIG_DIR);
            }
            Properties p = new Properties();
            p.setProperty("currentSourceIndex", String.valueOf(currentSourceIndex));
            p.setProperty("everyColumnsImgNum", String.valueOf(everyColumnsImgNum));
            p.setProperty("recentKeywords", String.join("||", recentKeywords));
            try (OutputStream os = Files.newOutputStream(CONFIG_FILE)) {
                p.store(os, "EmojiSearch config");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void load() {
        if (!Files.exists(CONFIG_FILE)) {
            return;
        }
        Properties p = new Properties();
        try (InputStream is = Files.newInputStream(CONFIG_FILE)) {
            p.load(is);
            currentSourceIndex = Integer.parseInt(p.getProperty("currentSourceIndex", "0"));
            everyColumnsImgNum = Integer.parseInt(p.getProperty("everyColumnsImgNum", "3"));
            recentKeywords.clear();
            String keywords = p.getProperty("recentKeywords", "");
            if (!keywords.trim().isEmpty()) {
                String[] parts = keywords.split("\\|\\|");
                for (String part : parts) {
                    if (!part.trim().isEmpty()) {
                        recentKeywords.add(part.trim());
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
