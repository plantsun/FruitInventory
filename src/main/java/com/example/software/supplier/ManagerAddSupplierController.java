package com.example.software.supplier;

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
class ManagerAddSupplierRequest {
    private String supplierName;
    private String telephone;
    private String location;
    private String operator;
}

@Data
@NoArgsConstructor
class ManagerAddSupplierResponse {
    private String flag;
}

@RestController
public class ManagerAddSupplierController {
    @PostMapping("/ManagerAddSupplier")
    public ResponseEntity<String> managerAddSupplier(@RequestBody ManagerAddSupplierRequest request) {
        String supplierName = request.getSupplierName();
        String telephone = request.getTelephone();
        String location = request.getLocation();
        String operator = request.getOperator();
        System.out.println("[ManagerAddSupplier]supplierName: " +  supplierName);
        System.out.println("[ManagerAddSupplier]telephone: " +  telephone);
        System.out.println("[ManagerAddSupplier]location: " +  location);
        System.out.println("[ManagerAddSupplier]operator: " + operator);

        ManagerAddSupplierResponse response = new ManagerAddSupplierResponse();
        // 注册逻辑
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null && business.checkAdmin())
        {
            String res = business.addSupplier(supplierName, telephone, location);
            if (res.equals("添加供应商成功"))
            {
                response.setFlag("1");
            }
            else
            {
                System.out.println("[ManagerAddSupplier]: " + res);
                response.setFlag("0");
            }
        }
        else
        {
            if (business == null)
                System.out.println("[ManagerAddSupplier]用户未登录");
            else
                System.out.println("[ManagerAddSupplier]权限不足: " + business.checkAdmin());
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[ManagerAddSupplier]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}
