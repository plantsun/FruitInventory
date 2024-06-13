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
class RemoveFruitRequest {
    private int fruitId; // 实际上是存储ID
    private int number;
    private String operator;
}

@Data
@NoArgsConstructor
class RemoveFruitResponse {
    private String flag;
}

@RestController
public class RemoveFruitController {
    @PostMapping("/removeFruit")
    public ResponseEntity<String> removeFruit(@RequestBody RemoveFruitRequest request)
    {
        int fruitId = request.getFruitId();
        int number = request.getNumber();
        String operator = request.getOperator();
        System.out.println("[RemoveFruit]fruitId: " + fruitId);
        System.out.println("[RemoveFruit]number: " + number);
        System.out.println("[RemoveFruit]operator: " + operator);

        RemoveFruitResponse response = new RemoveFruitResponse();
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null)
        {
            String res = business.outStorage(fruitId, number);
            if (res.equals("出库成功"))
            {
                response.setFlag("1");
            }
            else
            {
                System.out.println("[RemoveFruit]: " + res);
                response.setFlag("0");
            }
        }
        else
        {
            System.out.println("[RemoveFruit]用户未登录");
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[RemoveFruit]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}
