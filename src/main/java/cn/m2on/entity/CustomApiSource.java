package cn.m2on.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户自定义 API 源定义
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomApiSource {
    private String sourceId;
    private String sourceName;
    private String url;
    private String keywordParam;
    private String listField;
    private String imageField;
}
