package com.example.software;

import com.baidu.ai.aip.utils.Base64Util;
import com.baidu.ai.aip.utils.FileUtil;
import com.baidu.ai.aip.utils.HttpUtil;
import com.google.gson.Gson;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URLEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class Location {
    private int left;
    private int top;
    private int width;
    private int height;

    Location(int top, int left, int width, int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    int getLeft()
    {
        return this.left;
    }

    int getTop()
    {
        return this.top;
    }

    int getWidth()
    {
        return this.width;
    }

    int getHeight()
    {
        return this.height;
    }
}

class Result {
    private double score;
    private String name;
    private Location location;

    Result(double score, String name, Location location) {
        this.score = score;
        this.name = name;
        this.location = location;
    }

    double getScore()
    {
        return this.score;
    }

    String getName()
    {
        return this.name;
    }

    Location getLocation()
    {
        return this.location;
    }
}

class Response {
    private long log_id;
    private List<Result> result;

    Response(long log_id, List<Result> result)
    {
        this.log_id = log_id;
        this.result = result;
    }

    List<Result> getResult()
    {
        return this.result;
    }
}

class FruitResult {
    private double score;
    private String name;

    FruitResult(String name, double score) {
        this.score = score;
        this.name = name;
    }

    double getScore()
    {
        return this.score;
    }

    String getName()
    {
        return this.name;
    }

}

class FruitResponse {
    private long log_id;
    private int result_num;
    private List<FruitResult> result;

    FruitResponse(long log_id, int result_num, List<FruitResult> result)
    {
        this.log_id = log_id;
        this.result_num = result_num;
        this.result = result;
    }

    List<FruitResult> getResult()
    {
        return this.result;
    }
}

/**
 * 细粒度图像识别
 */
public class Detect {
    private static final long FRAME_INTERVAL_MS = 1000; // 控制帧率，这里设置为每秒一帧
    private long lastFrameTime = 0;
    private static Mat srcImg;

    public static FruitResponse fruitDetect(Mat mat) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/classify/ingredient";
        try {
            // 本地文件路径
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", mat, matOfByte);

            byte[] imgData = matOfByte.toArray();
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.276a0f6e2484ceb5a9047daa599bc6dc.2592000.1720060782.282335-78324390";


//            long currentTime = Instant.now().toEpochMilli();
//            while (currentTime - lastFrameTime < FRAME_INTERVAL_MS) {
//                return new ResponseEntity<>("Frame rate too high", HttpStatus.TOO_MANY_REQUESTS);
//                currentTime = Instant.now().toEpochMilli();
//            }
//            lastFrameTime = currentTime;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);

            Gson gson = new Gson();
            return gson.fromJson(result, FruitResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Response multiObjectDetect(String filePath)
    {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/multi_object_detect";
        try {
            srcImg = Imgcodecs.imread(filePath);
            if (srcImg.empty())
            {
                throw new Exception("Could not load image");
            }
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.88cb127efa1c60b8638c66bb8f682705.2592000.1720062362.282335-78340331";

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);

            Gson gson = new Gson();
            return gson.fromJson(result, Response.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void detect(String filePath) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Response response = Detect.multiObjectDetect(filePath);
        if (response == null || response.getResult() == null)
        {
            throw new Exception("no result!");
        }
        boolean flag = false;
        Mat dstImg = srcImg;
        for (Result result : response.getResult())
        {
            if (result.getScore() < 0.4  || !result.getName().equals("果蔬生鲜"))   continue;
            flag = true;

            Location location = result.getLocation();
            int left = location.getLeft();
            int top = location.getTop();
            int width = location.getWidth();
            int height = location.getHeight();
            Rect rect = new Rect(new Point(left, top), new Point(left + width, top + height));

            Mat fruitImg = new Mat(dstImg, rect);
            FruitResponse fruitResponse = Detect.fruitDetect(fruitImg);
            if (fruitResponse == null || fruitResponse.getResult() == null || fruitResponse.getResult().isEmpty())
            {
                throw new Exception("no result!");
            }
            String fruitName = fruitResponse.getResult().get(0).getName();
            double fruitScore = fruitResponse.getResult().get(0).getScore();

            // 框出矩形框和文字
            Imgproc.rectangle(dstImg, rect, new Scalar(255, 0, 0));
            String label = fruitName + ": " + fruitScore;
            Imgproc.putText(dstImg, label, new Point(left, top), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 1);
        }

        if (flag)
        {
            HighGui.imshow("Image", dstImg);
            HighGui.waitKey(0);
        }
        else
        {
            throw new Exception("no result!");
        }
    }

    public static void main(String[] args) {
        try {
            Detect.detect("./image/apple.jpg");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
