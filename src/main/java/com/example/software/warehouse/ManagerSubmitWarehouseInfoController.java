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
class ManagerSubmitWarehouseInfoRequest {
    private String warehouseName;
    private String telephone;
    private String location;
    private int warehouseId;
    private String operator;
}

@Data
@NoArgsConstructor
class  ManagerSubmitWarehouseInfoResponse {
    private String flag;
}

@RestController
public class ManagerSubmitWarehouseInfoController {
    @PostMapping("/ManagerSubmitWarehouseInfo")
    public ResponseEntity<String> managerSubmitWarehouseInfo(@RequestBody ManagerSubmitWarehouseInfoRequest request) {
        String warehouseName = request.getWarehouseName();
        String telephone = request.getTelephone();
        String location = request.getLocation();
        int warehouseId = request.getWarehouseId();
        String operator = request.getOperator();
        System.out.println("[ManagerSubmitWarehouseInfo]warehouseName: " + warehouseName);
        System.out.println("[ManagerSubmitWarehouseInfo]telephone: " + telephone);
        System.out.println("[ManagerSubmitWarehouseInfo]location: " + location);
        System.out.println("[ManagerSubmitWarehouseInfo]warehouseId: " + warehouseId);
        System.out.println("[ManagerSubmitWarehouseInfo]operator: " + operator);

        ManagerSubmitWarehouseInfoResponse response = new ManagerSubmitWarehouseInfoResponse();
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null && business.checkAdmin())   // 用户已登录且为管理员
        {
            String res = business.updateWarehouse(warehouseId, warehouseName, telephone, location);
            if (res.equals("更新仓库成功"))
            {
                response.setFlag("1");
            }
            else
            {
                System.out.println("ManagerSubmitWarehouseInfo: " + res);
                response.setFlag("0");
            }
        }
        else
        {
            if (business == null)
                System.out.println("[ManagerSubmitWarehouseInfo]用户未登录");
            else
                System.out.println("[ManagerSubmitWarehouseInfo]权限不足: " + business.checkAdmin());
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[ManagerSubmitWarehouseInfo]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}