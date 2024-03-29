# Emoji-Search

> 平时没有收集表情包的习惯，但是聊天有时候又想发一个生动的表情，怎么办呢。写一个关键字搜索表情的工具吧！
> **搜索表情后，点击表情后就可以将该表情复制到剪切板了！**

## 开发进度

- [x] 主页UI --3.9
- [x] 设置页面UI --3.12
- [x] 研究UI美化 --3.13
- [x] 表情爬取+显示 --3.14
- [x] 添加/更换爬虫源 --3.15
- [ ] ~~设置预览表情长度~~ --3.15 不必要的
- [x] 最小化托盘
- [x] 导入本地源 --3.15

***

**等有空了开发**

- [ ] 本地表情包库列表管理 
  - [ ] 保存本地库
  - [ ] 读取本地库
- [ ] 添加读取本地表情包库配置列表

## 结构

```cmd
src
 ├─main
    ├─java
    │  └─cn
    │      └─m2on
    │          ├─config --配置
    │          ├─crawler --爬虫
    │          │  └─provider --自定义源爬虫应该放在此处
    │          ├─data --爬虫数据
    │          ├─entity --数据实体类
    │          ├─ui --UI相关
    │          └─util --工具类
    │              └─ui
    └─resources 
    	└─config --图片资源
```



## 运行

运行 `cn.m2on.EmojiSearchApp` 即可

###### 运行截图
![a6fTVbWRR0](https://github.com/auuuu4/EmojiSearch/assets/97584318/6c7250ae-f3cc-4209-b588-6d26e1f5ab86)

![gog5j47RUT](https://github.com/auuuu4/EmojiSearch/assets/97584318/37f68a30-592f-432c-aebe-a3da65248e26)

![lHMeoSsPGZ](https://github.com/auuuu4/EmojiSearch/assets/97584318/a0906e82-6a76-44c4-bb05-c6eaba9aba12)

## 开发

自定义爬虫源，只需两步：

1.  实现 `cn/m2on/crawler/SourceProvider.java` 接口，可以参照 `cn/m2on/crawler/provider` 内已经实现的类。

   **这个接口的最终目标是向 `SearchData.imgURLSources` 里添加所有需要显示图片的 `URL` 。**

   ###### 你可以使用 `cn/m2on/crawler/ImageThreadPoolExecutor.java` 的 `execute()` 方法来添加一个或多个线程（最大值10，如果需要更多线程，修改创建线程池的构造方法）帮助更快地爬取图片。

2. 在 `cn/m2on/data/SearchData.java` 的 `static` 块中添加记录。

   类似于这样

```JAVA
	// 添加初始爬虫
    static {
        crawlers.add(new AlapiProvider());
        crawlers.add(new RandomProvider());
    }
    // 添加初始源
    static {
        HashMap<String,Object> params = new HashMap<>();
        params.put("token","xxxxxxxxx");
        crawlerSourcesArray.add(new CrawlerURLSource("ALAPI源","https://v2.alapi.cn/api/doutu", Method.GET,"keyword",params));
        params.clear();
        params.put("return","json");
        params.put("num",100);
        crawlerSourcesArray.add(new CrawlerURLSource("随机图片源","https://img.moehu.org/pic.php",Method.GET,"keyword",params));


    }
 	
```

