package com.example.software.fruit;

import com.example.software.database.forms.StockList;
import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
class Fruit {
    private int fruitId;
    private String class_;
    private String qualify;
    private int warehouseId;
    private int num;
}

class GetFruitInfoResponse {
    private List<Fruit> fruits;
    private int total = 0;

    GetFruitInfoResponse(List<Fruit> fruits) {
        this.fruits = fruits;
        total = fruits.size();
    }
}

@RestController
public class GetFruitInfoController {
    @Autowired
    private Business business;

    @GetMapping("/getFruitInfo")
    public String getFruitInfo() {
        List<Fruit> fruits = new ArrayList<>();
        for (StockList stock : business.getAllStock())
        {
            int fruitId = stock.getID();
            String class_ = stock.getFruitName();
            String quality = stock.getQuality();
            int warehouseId = stock.getWarehouseID();
            int num = stock.getNumber();
            fruits.add(new Fruit(fruitId, class_, quality, warehouseId, num));
        }
        GetFruitInfoResponse response = new GetFruitInfoResponse(fruits);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[GetFruitInfo]Response: " + jsonResponse);
        return jsonResponse;
    }
}
