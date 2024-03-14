package cn.m2on.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: m2on
 * @Date: 2024/03/12/8:29
 * @Description: 一条搜索结果，包含一张图片和相关信息
 */
@Data
@AllArgsConstructor
public class ImageSource {
    String imgUrl;
    Image image;
}
