package com.example.software.fruit;

import com.example.software.InformationBuffer;
import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
class ThresholdRequest {
    private int highThreshold;
    private int lowThreshold;
    private String operator;
}

@AllArgsConstructor
class ThresholdResponse {
    private List<Integer> lowThresholdIds;
    private List<Integer> highThresholdIds;
}

@RestController
public class ThresholdController {
    @PostMapping("/threshold")
    public ResponseEntity<String> threshold(@RequestBody ThresholdRequest thresholdRequest)
    {
        int highThreshold = thresholdRequest.getHighThreshold();
        int lowThreshold = thresholdRequest.getLowThreshold();
        String operator = thresholdRequest.getOperator();
        System.out.println("[Threshold]highThreshold: " + highThreshold);
        System.out.println("[Threshold]lowThreshold: " + lowThreshold);
        System.out.println("[Threshold]operator: " + operator);

        Business business = InformationBuffer.getBusiness(operator);
        List<Integer> lowThresholdIds = new ArrayList<>(), highThresholdIds = new ArrayList<>();
        if (business != null)
        {
            List<List<Integer>> idLists = business.checkStockByLimit(lowThreshold, highThreshold);

            if (idLists.get(0) != null)
            {
                lowThresholdIds = idLists.get(0);
            }

            if (idLists.get(1) != null)
            {
                highThresholdIds = idLists.get(1);
            }
        }
        else
        {
            System.out.println("[Threshold]用户未登录");
        }
        ThresholdResponse response = new ThresholdResponse(lowThresholdIds, highThresholdIds);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[Threshold]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}
