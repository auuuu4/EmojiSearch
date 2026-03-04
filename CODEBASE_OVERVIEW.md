# EmojiSearch 代码库概览

## 1. 这个项目是做什么的

EmojiSearch 是一个基于 Java Swing 的桌面小工具，用来按关键词搜索网络表情图并展示结果。用户点击结果图后，图片会被复制到系统剪贴板，便于在聊天场景中直接粘贴发送。

## 2. 功能范围

- 关键词搜索：在主界面输入关键词并回车，触发搜索。
- 多源抓取：支持通过可插拔 `SourceProvider` 使用不同 API 作为表情来源。
- 并发下载：URL 解析与图片下载分离，图片抓取使用线程池并发执行。
- 实时渲染：主窗口中有持续消费队列的线程，下载到图后就增量插入 UI。
- 点击复制：点击任意图片按钮，将图片写入系统剪贴板。
- 系统托盘：可最小化到托盘，单击托盘图标恢复窗口。
- 设置页：可以切换抓取源，并可导入本地图片目录进行展示。

## 3. 核心架构

项目采用“UI 层 + 数据队列层 + Provider 抓取层 + 图片下载执行层”的结构：

1. **UI 层**
   - `Index`：主窗口，负责搜索输入、图片展示、托盘、点击复制。
   - `Setting`：设置窗口，负责切换源、导入本地目录。
2. **数据队列层**
   - `SearchData`：集中维护抓取源配置、URL 队列、图片队列、Provider 列表。
3. **Provider 抓取层**
   - `SourceProvider` 接口：定义 `provideSource(keyword)`。
   - `DouTuApiProvider` / `RandomProvider` / `AlapiProvider` / `LocalSourceProvider`：实现不同来源的 URL 收集与解析。
4. **图片下载执行层**
   - `ImageThreadPoolExecutor`：统一管理线程池。
   - `ImgCrawler`：从 URL 队列取任务、下载图片、写入图片队列。

## 4. 关键执行流程（搜索）

1. 用户在 `Index.searchTextField` 回车。
2. `Index` 清空 URL 队列并重置线程池。
3. 调用当前选中 `SourceProvider` 的 `provideSource(keyword)`，将图片 URL 写入 `SearchData.imgURLQueue`。
4. 调用 `ImageThreadPoolExecutor.consume()` 启动 5 个 `ImgCrawler` 并发下载。
5. 主窗口中的 `imageUpdateThread` 持续阻塞读取 `SearchData.imageQueue`，一有图片就插入网格面板。
6. 用户点击图片按钮，图片进入系统剪贴板。

## 5. 目录说明（主干）

- `src/main/java/cn/m2on/ui`：Swing 窗口与交互逻辑。
- `src/main/java/cn/m2on/crawler`：抓取抽象与线程池/下载执行。
- `src/main/java/cn/m2on/crawler/provider`：各数据源 Provider 实现。
- `src/main/java/cn/m2on/data`：共享状态与阻塞队列。
- `src/main/java/cn/m2on/entity`：配置/图片等实体。
- `src/main/java/cn/m2on/util/ui`：UI 组件辅助工具。

## 6. 设计特点与注意事项

- **可扩展性**：新增源时，只需新增 `SourceProvider` 实现并注册到 `SearchData`。
- **解耦思路**：URL 发现与图片下载拆开，用阻塞队列衔接。
- **并发模型简单直接**：固定线程池 + 阻塞队列，易理解但缺乏更精细的生命周期管理。
- **状态集中但偏全局**：`SearchData` 为静态全局状态，便捷但耦合度较高。
- **UI 线程安全风险**：后台线程直接操作 Swing 组件，理论上应通过 EDT (`SwingUtilities.invokeLater`) 更新 UI。
- **网络健壮性可提升**：部分 Provider 直接吞异常或使用固定下标访问配置，对配置变更较敏感。

## 7. 运行方式

- 入口：`cn.m2on.EmojiSearchApp`。
- 构建工具：Maven。
- Java 版本：`maven-compiler-plugin` 目标为 Java 8。

