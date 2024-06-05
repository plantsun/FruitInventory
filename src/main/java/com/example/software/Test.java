package com.example.software;

import java.io.File;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        // 生成一个唯一的文件名
        String uniqueFileName = UUID.randomUUID().toString() + "-" + "a.jpg";

        // 设置保存路径
        String savePath = "./image/";

        // 确保目录存在
        File saveDirectory = new File(savePath);
    }
}
