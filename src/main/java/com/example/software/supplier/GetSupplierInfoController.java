package com.example.software.supplier;

import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
class Supplier {
    private String supplierName;
    private String telephone;
    private String location;
    private int supplierId;
}

@AllArgsConstructor
class GetSupplierInfoResponse {
    private List<Supplier> supplier;
}

@RestController
public class GetSupplierInfoController {
    @Autowired
    private Business business;

    @GetMapping("/getSupplierInfo")
    public String getSupplierInfo() {
        List<Supplier> supplierList = new ArrayList<>();
        for (com.example.software.database.table.Supplier supplier : business.getAllSupplier())
        {
            String supplierName = supplier.getName();
            String telephone = supplier.getTelephone();
            String location = supplier.getLocation();
            int supplierId = supplier.getSupplierID();
            supplierList.add(new Supplier(supplierName, telephone, location, supplierId));
        }
        GetSupplierInfoResponse response = new GetSupplierInfoResponse(supplierList);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[GetSupplierInfo]Response: " + jsonResponse);
        return jsonResponse;
    }
}