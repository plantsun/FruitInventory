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

class ManagerAddUserRequest {
    private String username;
    private String password;
    private String warehouseName;
    private String isManager;
    private String operator;

    ManagerAddUserRequest(String username, String password, String warehouseName, String isManager, String operator)
    {
        this.username = username;
        this.password = password;
        this.warehouseName = warehouseName;
        this.isManager = isManager;
        this.operator = operator;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public String getIsManager() {
        return isManager;
    }

    public String getOperator() {
        return operator;
    }
}

class ManagerAddUserResponse {
    private String flag;

    ManagerAddUserResponse() {}

    void setFlag(String flag) {
        this.flag = flag;
    }
}

@RestController
public class ManagerAddUserController {
    @PostMapping("/ManagerAddUser")
    public ResponseEntity<String> managerAddUser(@RequestBody ManagerAddUserRequest managerAddUserRequest) {
        String username = managerAddUserRequest.getUsername();
        String password = managerAddUserRequest.getPassword();
        String warehouseName = managerAddUserRequest.getWarehouseName();
        short isManager = (short) (managerAddUserRequest.getIsManager().equals("yes") ? 1 : 0);
        String operator = managerAddUserRequest.getOperator();
        System.out.println("[ManagerAddUser]username: " + username);
        System.out.println("[ManagerAddUser]password: " + password);
        System.out.println("[ManagerAddUser]warehouseName: " + warehouseName);
        System.out.println("[ManagerAddUser]isManager: " + isManager);
        System.out.println("[ManagerAddUser]operator: "  + operator);

        ManagerAddUserResponse response = new ManagerAddUserResponse();
        // 注册逻辑
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null && business.checkAdmin())
        {
            String res = business.addUser(username, password, warehouseName, isManager);
            if (res.equals("该用户名已存在"))
            {
                response.setFlag("2");
            }
            else if (res.equals("注册成功"))
            {
                response.setFlag("1");
            }
            else    // 注册失败
            {
                System.out.println("ManagerAddUser: " + res);
                response.setFlag("0");
            }
        }
        else
        {
            if (business == null)
                System.out.println("[ManagerAddUser]用户未登录");
            else
                System.out.println("[ManagerAddUser]权限不足: " + business.checkAdmin());
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[ManagerAddUser]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}
