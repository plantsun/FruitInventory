package com.example.software;

import com.google.gson.Gson;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
//        Map<String, String> res = new HashMap<>();
//        res.put("type", "苹果");
//        res.put("quality", "high");
//        ImageResponse imageResponse = new ImageResponse(res.get("type"), res.get("quality"));
//        Gson gson = new Gson();
//        String jsonResponse = gson.toJson(imageResponse);
//        System.out.println(jsonResponse);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        Mat img = Imgcodecs.imread("E:\\study\\java\\software\\image\\21b7cf14-7844-4ce7-90d9-3ed0c012cff3-QQ图片20240605165258.jpg");
        Mat img = Imgcodecs.imread("E:\\study\\java\\software\\image\\apple.jpg");
        HighGui.imshow("test", img);
        HighGui.waitKey(0);
    }
}
