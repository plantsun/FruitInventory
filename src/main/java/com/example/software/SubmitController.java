package com.example.software;

import com.example.software.database.serves.Business;
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
    private String warehouseName;
    private String quality;
    private Timestamp createdAt;
    private String operator;

    // 构造函数
    public SubmitRequest(String fruitType, String warehouseName, String quality, Timestamp createdAt, String operator) {
        this.operator = operator;
        this.fruitType = fruitType;
        this.warehouseName = warehouseName;
        this.quality = quality;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public String getOperator() {
        return this.operator;
    }

    public String getFruitType() {
        return this.fruitType;
    }

    public String getWarehouseName() {
        return this.warehouseName;
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
    private String message;

    SubmitResponse() {}

    void setFlag(String flag) {
        this.flag = flag;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

@RestController
public class SubmitController {
    @PostMapping("/submit")
    public ResponseEntity<String> submit(@RequestBody SubmitRequest submitRequest) {
        String operator = submitRequest.getOperator();
        String fruitType = submitRequest.getFruitType();
        String warehouseName = submitRequest.getWarehouseName();
        String quality = submitRequest.getQuality();
        Timestamp createdAt = submitRequest.getCreatedAt();
        System.out.println("[Submit]operator: " + operator);
        System.out.println("[Submit]fruitType: " + fruitType);
        System.out.println("[Submit]warehouseName: " + warehouseName);
        System.out.println("[Submit]quality: " + quality);
        System.out.println("[Submit]Timestamp: " + createdAt.toString());

        SubmitResponse submitResponse = new SubmitResponse();
        // 假设这里进行了提交处理
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null)
        {
            String[] res = business.storage(fruitType, warehouseName, createdAt, 1, quality);
            if (res[0].equals("入库成功"))
            {
                submitResponse.setFlag("1");
                submitResponse.setMessage(res[1]);
            }
            else
            {
                submitResponse.setFlag("0");
            }
        }
        else
        {
            submitResponse.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(submitResponse);
        System.out.println("[Submit]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}