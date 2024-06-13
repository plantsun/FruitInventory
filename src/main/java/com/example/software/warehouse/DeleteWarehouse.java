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
class DeleteWarehouseRequest {
    private int warehouseId;
    private String operator;
}

@Data
@NoArgsConstructor
class DeleteWarehouseResponse {
    private String flag;
}

@RestController
public class DeleteWarehouse {
    @PostMapping("/deleteWarehouse")
    public ResponseEntity<String> deleteWarehouse(@RequestBody DeleteWarehouseRequest request)
    {
        int warehouseId = request.getWarehouseId();
        String operator = request.getOperator();
        System.out.println("[DeleteWarehouse]warehouseId: " + warehouseId);
        System.out.println("[DeleteWarehouse]operator: " + operator);

        DeleteWarehouseResponse response = new DeleteWarehouseResponse();
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null && business.checkAdmin())
        {
            String res = business.deleteWarehouse(warehouseId);
            if (res.equals("删除仓库成功"))
            {
                response.setFlag("1");
            }
            else
            {
                System.out.println("[DeleteWarehouse]" + res);
                response.setFlag("0");
            }
        }
        else
        {
            if (business == null)
                System.out.println("[DeleteWarehouse]用户未登录");
            else
                System.out.println("[DeleteWarehouse]权限不足: " + business.checkAdmin());
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[DeleteWarehouse]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}