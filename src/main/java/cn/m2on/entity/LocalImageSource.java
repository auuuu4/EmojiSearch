package cn.m2on.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * @Author: auuuu4
 * @Date: 2024/06/24/15:24
 * @Description: 本地图片源
 */
@AllArgsConstructor
@Data
public class LocalImageSource {
    String path;
    Image image;

}
