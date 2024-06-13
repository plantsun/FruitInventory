package com.example.software.user;

import com.example.software.InformationBuffer;
import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

class ManagerSubmitUserInfoRequest {
    private int userId;
    private String username;
    private String password;
    private String isManager;
    private int warehouseId;
    private String operator;

    ManagerSubmitUserInfoRequest(int userId, String username, String password, String isManager, int warehouseId, String operator)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.isManager = isManager;
        this.warehouseId = warehouseId;
        this.operator = operator;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIsManager() {
        return isManager;
    }

    public int getWarehouseId()
    {
        return warehouseId;
    }

    public String getOperator()
    {
        return operator;
    }
}

class ManagerSubmitUserInfoResponse {
    private String flag;

    ManagerSubmitUserInfoResponse() {}

    public void setFlag(String flag)
    {
        this.flag = flag;
    }
}

@RestController
public class ManagerSubmitUserInfoController {
    @PostMapping("/ManagerSubmitUserInfo")
    public ResponseEntity<String> managerSubmitUserInfo(@RequestBody ManagerSubmitUserInfoRequest submitRequest) {
        int id = submitRequest.getUserId();
        String username = submitRequest.getUsername();
        String password = submitRequest.getPassword();
        short isManager = (short) (submitRequest.getIsManager().equals("yes") ? 1 : 0);
        int warehouseId = submitRequest.getWarehouseId();
        String operator = submitRequest.getOperator();
        System.out.println("[ManagerSubmitUserInfo]id: " + id);
        System.out.println("[ManagerSubmitUserInfo]username: " + username);
        System.out.println("[ManagerSubmitUserInfo]password: " + password);
        System.out.println("[ManagerSubmitUserInfo]isManager: " + isManager);
        System.out.println("[ManagerSubmitUserInfo]warehouseId: " + warehouseId);
        System.out.println("[ManagerSubmitUserInfo]operator: " + operator);

        ManagerSubmitUserInfoResponse response = new ManagerSubmitUserInfoResponse();
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null && business.checkAdmin())   // 用户已登录且为管理员
        {
            String res = business.updateUser(id, username, password, isManager, warehouseId);
            if (res.equals("更新用户信息成功"))
            {
                response.setFlag("1");
                Business business1 = InformationBuffer.getBusiness(id);
                if (business1 != null)  // 假如用户已登录，则修改business中的用户信息
                {
                    business1.getUser().setUserName(username);
                    business1.getUser().setPassword(password);
                    business1.getUser().setPriority(isManager);
                    business1.getUser().setWarehouseID(warehouseId);
                }
            }
            else
            {
                System.out.println("[ManagerSubmitUserInfo]: " + res);
                response.setFlag("0");
            }
        }
        else
        {
            if (business == null)
                System.out.println("[ManagerSubmitUserInfo]用户未登录");
            else
                System.out.println("[ManagerSubmitUserInfo]权限不足: " + business.checkAdmin());
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[ManagerSubmitUserInfo]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}