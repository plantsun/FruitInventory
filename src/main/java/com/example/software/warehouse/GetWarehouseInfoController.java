package com.example.software.warehouse;

import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
class Warehouse {
    private String warehouseName;
    private String telephone;
    private String location;
    private int warehouseId;
}

@AllArgsConstructor
class GetWarehouseInfoResponse {
    List<Warehouse> warehouse;
}

@RestController
public class GetWarehouseInfoController {
    @Autowired
    private Business business;

    @GetMapping("/getWarehouseInfo")
    public String getWarehouseInfo() {
        List<Warehouse> warehouseList = new ArrayList<>();
        for (com.example.software.database.table.Warehouse warehouse : business.getAllWarehouse())
        {
            String warehouseName = warehouse.getName();
            String telephone = warehouse.getTelephone();
            String location = warehouse.getLocation();
            int warehouseId = warehouse.getWarehouseID();
            warehouseList.add(new Warehouse(warehouseName, telephone, location, warehouseId));
        }
        GetWarehouseInfoResponse response = new GetWarehouseInfoResponse(warehouseList);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[GetWarehouseInfo]Response: " + jsonResponse);
        return jsonResponse;
    }

}
