package com.example.software;

import com.google.gson.Gson;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

import java.util.ArrayList;
import java.util.List;

public class DetectTest {
    List<Mat> apple_imgs = new ArrayList<Mat>();
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String filePath = "./image/apple.jpg";
        Mat img = Imgcodecs.imread(filePath);
        String s = "{\"result\":[{\"score\":0.6434519,\"name\":\"果蔬生鲜\",\"location\":{\"top\":44,\"left\":270,\"width\":80,\"height\":87}},{\"score\":0.5950581,\"name\":\"果蔬生鲜\",\"location\":{\"top\":162,\"left\":60,\"width\":66,\"height\":60}},{\"score\":0.4127536,\"name\":\"果蔬生鲜\",\"location\":{\"top\":221,\"left\":156,\"width\":80,\"height\":79}},{\"score\":0.30627492,\"name\":\"果蔬生鲜\",\"location\":{\"top\":35,\"left\":37,\"width\":332,\"height\":274}},{\"score\":0.19713111,\"name\":\"果蔬生鲜\",\"location\":{\"top\":152,\"left\":129,\"width\":71,\"height\":78}},{\"score\":0.65332574,\"name\":\"果蔬生鲜\",\"location\":{\"top\":122,\"left\":223,\"width\":76,\"height\":70}}],\"log_id\":1797907589287265486}";
        Gson gson = new Gson();
        Response response = gson.fromJson(s, Response.class);
        for (Result result : response.getResult())
        {
            if (result.getScore() < 0.4  || !result.getName().equals("果蔬生鲜"))   continue;
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
//          String label = "apple" + ": " + result.getScore();
//          Imgproc.putText(img, label, new Point(left, top), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 1);
        }

        HighGui.imshow("apple", img);
        HighGui.waitKey(0);
    }
}