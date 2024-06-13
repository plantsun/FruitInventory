package com.example.software;

import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

class ImageResponse {
    private String message;
    private String fileName;
    private String type;
    private String quality;

    public ImageResponse(String type, String quality) {
        this.type = type;
        this.quality = quality;
    }

    public String getType() {
        return type;
    }

    public String getQuality() {
        return quality;
    }

    public String getMessage() {
        return message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

@Controller
public class ImageController {
    private static final long FRAME_INTERVAL_MS = 1000; // 控制帧率，这里设置为每秒一帧
    private long lastFrameTime = 0;

    @PostMapping("/upload-image")
    @ResponseBody
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        System.out.println("[Image]recv a image!!!");
        long currentTime = Instant.now().toEpochMilli();
        if (currentTime - lastFrameTime < FRAME_INTERVAL_MS) {
            return new ResponseEntity<>("Frame rate too high", HttpStatus.TOO_MANY_REQUESTS);
        }
        lastFrameTime = currentTime;

        // 检查是否接收到图像文件
        if (imageFile.isEmpty()) {
            return new ResponseEntity<>("No image received", HttpStatus.BAD_REQUEST);
        }
        try {
            // 获取图像文件名
            String originalFileName = imageFile.getOriginalFilename();

            // 生成一个唯一的文件名
            String uniqueFileName = UUID.randomUUID().toString() + "-" + originalFileName;

            // 设置保存路径
            String savePath = "E:\\study\\java\\software\\image\\";

            // 确保目录存在
            File saveDirectory = new File(savePath);
            if (!saveDirectory.exists())
            {
                System.out.println("目录不存在，正在创建目录...");
                if (!saveDirectory.mkdirs())
                {
                    throw new IOException("Failed to create directory");
                }
                System.out.println("目录创建成功!");
            }

            // 创建目标文件对象
            File dest = new File(savePath + uniqueFileName);
            // 如果目标文件已存在，则生成新的唯一文件名
            while (dest.exists()) {
                uniqueFileName = UUID.randomUUID().toString() + "-" + originalFileName;
                dest = new File(savePath + uniqueFileName);
            }
            System.out.println(dest.getAbsolutePath());

            // 将图像文件保存到目标文件
            imageFile.transferTo(dest);
            System.out.println("[Image]Image uploaded successfully");

            while(!dest.exists()){}
            Map<String, String> res = Detect.detect("src/main/resources/models/fruit_classifier.pt", dest.getAbsolutePath());
            ImageResponse imageResponse = new ImageResponse(res.get("type"), res.get("quality"));
            imageResponse.setMessage("Image uploaded successfully");
            imageResponse.setFileName(dest.getName());

            Gson gson = new Gson();
            String jsonResponse = gson.toJson(imageResponse);
            System.out.println("[Image]Response: " + jsonResponse);

            // 返回成功响应
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to detect image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}