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
class ManagerSubmitSupplierInfoRequest {
    private String supplierName;
    private String telephone;
    private String location;
    private int supplierId;
    private String operator;
}

@Data
@NoArgsConstructor
class ManagerSubmitSupplierInfoResponse {
    private String flag;
}

@RestController
public class ManagerSubmitSupplierInfoController {
    @PostMapping("/ManagerSubmitSupplierInfo")
    public ResponseEntity<String> managerSubmitSupplierInfo(@RequestBody ManagerSubmitSupplierInfoRequest request) {
        String supplierName = request.getSupplierName();
        String telephone = request.getTelephone();
        String location = request.getLocation();
        int supplierId = request.getSupplierId();
        String operator = request.getOperator();
        System.out.println("[ManagerSubmitSupplierInfo]supplierName: " + supplierName);
        System.out.println("[ManagerSubmitSupplierInfo]telephone: " + telephone);
        System.out.println("[ManagerSubmitSupplierInfo]location: " + location);
        System.out.println("[ManagerSubmitSupplierInfo]supplierId: " + supplierId);
        System.out.println("[ManagerSubmitSupplierInfo]operator: " + operator);

        ManagerSubmitSupplierInfoResponse response = new ManagerSubmitSupplierInfoResponse();
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null && business.checkAdmin())   // 用户已登录且为管理员
        {
            String res = business.updateSupplier(supplierId, supplierName, telephone, location);
            if (res.equals("更新供应商成功"))
            {
                response.setFlag("1");
            }
            else
            {
                System.out.println("[ManagerSubmitSupplierInfo]: " + res);
                response.setFlag("0");
            }
        }
        else
        {
            if (business == null)
                System.out.println("[ManagerSubmitSupplierInfo]用户未登录");
            else
                System.out.println("[ManagerSubmitSupplierInfo]权限不足: " + business.checkAdmin());
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[ManagerSubmitSupplierInfo]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}
