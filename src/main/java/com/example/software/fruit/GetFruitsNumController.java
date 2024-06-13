package com.example.software.fruit;

import com.example.software.database.forms.StockList;
import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
class GetFruitsNumResponse {
    private int AppleNum = 0;
    private int BananaNum = 0;
    private int OrangeNum = 0;
    private int GrapeNum = 0;
    private int PearNum = 0;
    private int GoodAppleNum = 0;
    private int BadAppleNum = 0;
    private int GoodBananaNum = 0;
    private int BadBananaNum = 0;
    private int GoodOrangeNum = 0;
    private int BadOrangeNum = 0;
    private int GoodGrapeNum = 0;
    private int BadGrapeNum = 0;
    private int GoodPearNum = 0;
    private int BadPearNum = 0;

    public GetFruitsNumResponse() {}
}

@RestController
public class GetFruitsNumController {
    @Autowired
    private Business business;

    @GetMapping("/getFruitsNum")
    public String getFruitsNum()
    {
        GetFruitsNumResponse response = new GetFruitsNumResponse();
        for (StockList stock : business.getAllStock())
        {
            switch (stock.getFruitName()) {
                case "Apple" -> {
                    response.setAppleNum(response.getAppleNum() + 1);
                    if (stock.getQuality().equals("Good")) {
                        response.setGoodAppleNum(response.getGoodAppleNum() + 1);
                    } else {
                        response.setBadAppleNum(response.getBadAppleNum() + 1);
                    }
                }
                case "Banana" -> {
                    response.setBananaNum(response.getBananaNum() + 1);
                    if (stock.getQuality().equals("Good")) {
                        response.setGoodBananaNum(response.getGoodBananaNum() + 1);
                    } else {
                        response.setBadBananaNum(response.getBadBananaNum() + 1);
                    }
                }
                case "Orange" -> {
                    response.setOrangeNum(response.getOrangeNum() + 1);
                    if (stock.getQuality().equals("Good")) {
                        response.setGoodOrangeNum(response.getGoodOrangeNum() + 1);
                    } else {
                        response.setBadOrangeNum(response.getBadOrangeNum() + 1);
                    }
                }
                case "Grape" -> {
                    response.setGrapeNum(response.getGrapeNum() + 1);
                    if (stock.getQuality().equals("Good")) {
                        response.setGoodGrapeNum(response.getGoodGrapeNum() + 1);
                    } else {
                        response.setBadGrapeNum(response.getBadGrapeNum() + 1);
                    }
                }
                case "Pear" -> {
                    response.setPearNum(response.getPearNum() + 1);
                    if (stock.getQuality().equals("Good")) {
                        response.setGoodPearNum(response.getGoodPearNum() + 1);
                    } else {
                        response.setBadPearNum(response.getBadPearNum() + 1);
                    }
                }
                default -> {
                }
            }
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[GetFruitNum]Response: " + jsonResponse);
        return jsonResponse;
    }
}