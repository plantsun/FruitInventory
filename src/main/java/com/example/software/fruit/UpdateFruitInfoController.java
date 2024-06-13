package com.example.software.fruit;

import com.example.software.InformationBuffer;
import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Data
@AllArgsConstructor
class UpdateFruitInfoRequest {
    private int fruitId;
    private String class_;
    private String qualify;
    private String operator;
}

@Data
@NoArgsConstructor
class UpdateFruitInfoResponse {
    private String flag;
}

@RestController
public class UpdateFruitInfoController {
    @PostMapping("/updateFruitInfo")
    public ResponseEntity<String> updateFruitInfo(@RequestBody UpdateFruitInfoRequest updateFruitInfoRequest)
    {
        int fruitId = updateFruitInfoRequest.getFruitId();
        String fruitName = updateFruitInfoRequest.getClass_();
        String qualify = updateFruitInfoRequest.getQualify();
        String operator = updateFruitInfoRequest.getOperator();
        System.out.println("[UpdateFruitInfo]fruitId: " + fruitId);
        System.out.println("[UpdateFruitInfo]class_: " + fruitName);
        System.out.println("[UpdateFruitInfo]qualify: " + qualify);
        System.out.println("[UpdateFruitInfo]operator: " + operator);

        UpdateFruitInfoResponse response = new UpdateFruitInfoResponse();
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null)
        {
            String res = business.updateStock(fruitId, fruitName, qualify);
            if (res.equals("存储信息修改成功"))
            {
                response.setFlag("1");
            }
            else
            {
                System.out.println("[UpdateFruitInfo]: " + res);
                response.setFlag("0");
            }
        }
        else
        {
            System.out.println("[UpdateFruitInfo]用户未登录");
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[UpdateFruitInfo]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}
