package com.example.software;

import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

class SubmitRequest {
    private String fruitType;
    private String quality;
    private Timestamp createdAt;

    // 构造函数
    public SubmitRequest(String fruitType, String quality, Timestamp createdAt) {
        this.fruitType = fruitType;
        this.quality = quality;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public String getFruitType() {
        return this.fruitType;
    }

    public String getQuality() {
        return quality;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
class SubmitResponse {
    private String flag;

    SubmitResponse() {}

    void setFlag(String flag) {
        this.flag = flag;
    }
}

@RestController
public class SubmitController {
    @PostMapping("/submit")
    public ResponseEntity<String> submit(@RequestBody SubmitRequest submitRequest) {
        String fruitType = submitRequest.getFruitType();
        String quality = submitRequest.getQuality();
        Timestamp createdAt = submitRequest.getCreatedAt();
        System.out.println("[Submit]fruitType: " + fruitType);
        System.out.println("[Submit]quality: " + quality);
        System.out.println("[Submit]Timestamp: " + createdAt.toString());

        // 假设这里进行了提交处理
        SubmitResponse submitResponse = new SubmitResponse();
        submitResponse.setFlag("1");

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(submitResponse);
        System.out.println("[Submit]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}