package com.example.software;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import com.baidu.ai.aip.utils.Base64Util;
import com.baidu.ai.aip.utils.FileUtil;
import com.baidu.ai.aip.utils.HttpUtil;
import com.google.gson.Gson;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLEncoder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

class OutCome {
    private String type;
    private String quality;

    OutCome(String type, String quality)
    {
        this.type = type;
        this.quality = quality;
    }

    String getType() {
        return this.type;
    }

    String getQuality() {
        return this.quality;
    }
}

/**
 * 细粒度图像识别
 */
public class Detect {
    private static final long FRAME_INTERVAL_MS = 1000; // 控制帧率，这里设置为每秒一帧
    private static long lastFrameTime = 0;
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

            long currentTime = Instant.now().toEpochMilli();
            while (currentTime - lastFrameTime < FRAME_INTERVAL_MS) {
                currentTime = Instant.now().toEpochMilli();
            }
            lastFrameTime = currentTime;

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
            System.out.println("filePath: " + filePath);
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

            long currentTime = Instant.now().toEpochMilli();
            while (currentTime - lastFrameTime < FRAME_INTERVAL_MS) {
                currentTime = Instant.now().toEpochMilli();
            }
            lastFrameTime = currentTime;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);

            Gson gson = new Gson();
            return gson.fromJson(result, Response.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> detect(String filePath) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Response response = Detect.multiObjectDetect(filePath);
        if (response == null || response.getResult() == null)
        {
            throw new Exception("no result!");
        }
        boolean flag = false;
        Mat dstImg = srcImg;
        String type = "";
        double fruitScore = 0;
        String quality = "";
        for (Result result : response.getResult())
        {
            if (result.getScore() < 0.4  || !(result.getName().equals("果蔬生鲜") || result.getName().equals("食品饮料")))   continue;

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
            for (FruitResult fruitResult : fruitResponse.getResult())
            {
                type = fruitResult.getName();
                fruitScore = fruitResult.getScore();
                if (!type.equals("非果蔬食材") && fruitScore > 0.4)  break;
            }

            // 框出矩形框和文字
            Imgproc.rectangle(dstImg, rect, new Scalar(255, 0, 0));
            String label = type + ": " + fruitScore;
            Imgproc.putText(dstImg, label, new Point(left, top), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 1);

            flag = true;
            break;
        }

        if (flag)
        {
//            HighGui.imshow("Image", dstImg);
//            HighGui.waitKey(0);
            Map<String, String> res = new HashMap<>();
            if (fruitScore > 0.6)
            {
                quality = "Great";
            }
            else
            {
                quality = "Bad";
            }
            res.put("type", type);
            res.put("quality", quality);
            return res;
        }
        else
        {
            throw new Exception("no result!");
        }
    }

    public static Map<String, String> detect(String ptPath, String imgPath) throws Exception {
        Path modelPath = Paths.get(ptPath);

        // Load the model
        try (Model model = Model.newInstance("fruit_classifier")) {
            model.load(modelPath);

            // Define the translator
            Translator<Image, OutCome> translator = new Translator<Image, OutCome>() {
                private final List<String> fruitTypes = Arrays.asList("Banana", "Apple", "Pear", "Orange", "Grape");
                private final List<String> qualities = Arrays.asList("Good", "Bad");

                @Override
                public NDList processInput(TranslatorContext ctx, Image image) {
                    NDArray ndArray = image.toNDArray(ctx.getNDManager());
                    Resize resize = new Resize(32, 32);
                    ndArray = resize.transform(ndArray);
                    ToTensor toTensor = new ToTensor();
                    ndArray = toTensor.transform(ndArray);
                    ndArray = ndArray.expandDims(0);
                    return new NDList(ndArray);
                }

                @Override
                public OutCome processOutput(TranslatorContext ctx, NDList list) {
                    // Process the model output and return Classifications
                    if (list.size() != 2) {
                        throw new IllegalArgumentException("Expected two outputs from the model");
                    }

                    NDArray typeOutput = list.get(0);
                    NDArray qualityOutput = list.get(1);

                    long typeIndex = typeOutput.argMax().getLong();
                    long qualityIndex = qualityOutput.argMax().getLong();

                    String typeLabel = fruitTypes.get((int) typeIndex);
                    String qualityLabel = qualities.get((int) qualityIndex);

                    return new OutCome(typeLabel, qualityLabel);
                }

                @Override
                public Batchifier getBatchifier() {
                    return null;
                }
            };

            // Prepare the image
            Image img = ImageFactory.getInstance().fromFile(Paths.get(imgPath));

            // Run inference
            try (Predictor<Image, OutCome> predictor = model.newPredictor(translator)) {
                OutCome result = predictor.predict(img);
                Map<String, String> res = new HashMap<>();
                res.put("type", result.getType());
                res.put("quality", result.getQuality());
                return res;
            } catch (TranslateException e) {
                e.printStackTrace();
            }
        }
        throw new Exception("no result!");
    }

    public static void main(String[] args) {
        try {
            Detect.detect("src/main/resources/models/fruit_classifier.pt","./image/bad.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
