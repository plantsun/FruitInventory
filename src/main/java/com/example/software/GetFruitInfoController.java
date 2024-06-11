package com.example.software;

import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

class Fruit {
    private int FruitId;
    private String class_;
    private String qualify;

    Fruit(int FruitId, String class_, String qualify)
    {
         this.FruitId = FruitId;
         this.class_ = class_;
         this.qualify = qualify;
    }
}

class GetFruitInfoResponse {
    private List<Fruit> fruits;
    int total;

    GetFruitInfoResponse(List<Fruit> fruits) {
        this.fruits = fruits;
        total = fruits.size();
    }
}

@RestController
public class GetFruitInfoController {
    @GetMapping("/getFruitInfo")
    public String getFruitInfo() {
        Business business = new Business();
        List<Fruit> fruits = new ArrayList<>();
        for (com.example.software.database.table.Fruit fruit : business.getAllFruit())
        {
            int fruitId = fruit.getFruitID();
            String class_ = fruit.getName();
            String qualify = fruit.getType();
            fruits.add(new Fruit(fruitId, class_, qualify));
        }
        GetFruitInfoResponse response = new GetFruitInfoResponse(fruits);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[GetFruitInfo]Response: " + jsonResponse);
        return jsonResponse;
    }
}
