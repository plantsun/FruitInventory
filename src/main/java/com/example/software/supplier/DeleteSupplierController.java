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
class DeleteSupplierRequest {
    private int supplierId;
    private String operator;
}

@Data
@NoArgsConstructor
class DeleteSupplierResponse {
    private String flag;
}

@RestController
public class DeleteSupplierController {
    @PostMapping("/deleteSupplier")
    public ResponseEntity<String> deleteSupplier(@RequestBody DeleteSupplierRequest request)
    {
        int supplierId = request.getSupplierId();
        String operator = request.getOperator();
        System.out.println("[DeleteSupplier]id: " + supplierId);
        System.out.println("[DeleteSupplier]operator: " + operator);

        DeleteSupplierResponse response = new DeleteSupplierResponse();
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null && business.checkAdmin())
        {
            String res = business.deleteSupplier(supplierId);
            if (res.equals("删除供应商成功"))
            {
                response.setFlag("1");
            }
            else
            {
                System.out.println("[DeleteSupplier]: " + res);
                response.setFlag("0");
            }
        }
        else
        {
            if (business == null)
                System.out.println("[DeleteSupplier]用户未登录");
            else
                System.out.println("[DeleteSupplier]权限不足: " + business.checkAdmin());
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[DeleteSupplier]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}
