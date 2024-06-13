package com.example.software.warehouse;

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
class ManagerAddWarehouseRequest {
    private String warehouseName;
    private String telephone;
    private String location;
    private String operator;
}

@Data
@NoArgsConstructor
class ManagerAddWarehouseResponse {
    private String flag;
}

@RestController
public class ManagerAddWarehouseController {
    @PostMapping("/ManagerAddWarehouse")
    public ResponseEntity<String> managerAddWarehouse(@RequestBody ManagerAddWarehouseRequest request)
    {
        String warehouseName = request.getWarehouseName();
        String telephone = request.getTelephone();
        String location = request.getLocation();
        String operator = request.getOperator();
        System.out.println("[ManagerAddWarehouse]warehouseName: " + warehouseName);
        System.out.println("[ManagerAddWarehouse]telephone: " + telephone);
        System.out.println("[ManagerAddWarehouse]location: " + location);
        System.out.println("[ManagerAddWarehouse]operator: " + operator);

        ManagerAddWarehouseResponse response = new ManagerAddWarehouseResponse();
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null && business.checkAdmin())
        {
            String res = business.addWarehouse(warehouseName, telephone, location);
            if (res.equals("添加仓库成功"))
            {
                response.setFlag("1");
            }
            else
            {
                System.out.println("[ManagerAddWarehouse]: " + res);
                response.setFlag("0");
            }
        }
        else
        {
            if (business == null)
                System.out.println("[ManagerAddWarehouse]用户未登录");
            else
                System.out.println("[ManagerAddWarehouse]权限不足: " + business.checkAdmin());
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[ManagerAddWarehouse]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}
