package com.example.software;

import com.google.gson.Gson;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.translate.TranslatorFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DetectTest {
    public void detect1() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String filePath = "./image/bad.jpg";
        Mat img = Imgcodecs.imread(filePath);
        String s = "{\"result\":[{\"score\":0.7526455,\"name\":\"食品饮料\",\"location\":{\"top\":49,\"left\":96,\"width\":173,\"height\":178}}],\"log_id\":1798252231413943197}";
        Gson gson = new Gson();
        Response response = gson.fromJson(s, Response.class);
        for (Result result : response.getResult())
        {
//            if (result.getScore() < 0.4  || !result.getName().equals("果蔬生鲜"))   continue;
            System.out.println(result.getScore());
            System.out.println(result.getName());
            Location location = result.getLocation();
            int left = location.getLeft();
            int top = location.getTop();
            int width = location.getWidth();
            int height = location.getHeight();
            System.out.println(top + " " + left + " " + width + " " + height);
            Rect rect = new Rect(new Point(left, top), new Point(left + width, top + height));
            Imgproc.rectangle(img, rect, new Scalar(255, 0, 0));
            Mat fruitImg = new Mat(img, rect);
            HighGui.imshow("fruit", fruitImg);
            HighGui.waitKey(0);
            String label = "apple" + ": " + result.getScore();
            Imgproc.putText(img, label, new Point(left, top), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 1);
        }

        HighGui.imshow("fruit", img);
        HighGui.waitKey(0);
    }

    public void detect2() throws IOException, MalformedModelException, TranslateException {
        Path modelPath = Paths.get("src/main/resources/models/fruit_classifier.pt");
        String imagePath = "image/bad.jpg";

        // Load the model
        try (Model model = Model.newInstance("fruit_classifier")) {
            model.load(modelPath);

            // Define the translator
            Translator<Image, OutCome> translator = new Translator<Image, OutCome>() {
                private final List<String> fruitTypes = Arrays.asList("香蕉", "苹果", "梨", "橙子", "葡萄");
                private final List<String> qualities = Arrays.asList("合格", "不合格");

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
            Image img = ImageFactory.getInstance().fromFile(Paths.get(imagePath));

            // Run inference
            try (Predictor<Image, OutCome> predictor = model.newPredictor(translator)) {
                OutCome result = predictor.predict(img);
                System.out.println("type: " + result.getType());
                System.out.println("quality: " + result.getQuality());
            }
        }
    }

    public static void main(String[] args) throws TranslateException, MalformedModelException, IOException {
        DetectTest detectTest = new DetectTest();
        detectTest.detect2();
    }
}