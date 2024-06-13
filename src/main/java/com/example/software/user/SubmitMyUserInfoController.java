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

class SubmitMyUserInfoRequest {
    private int userId;
    private String username;
    private String password;

    SubmitMyUserInfoRequest(int userId, String username, String password)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
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
}

class SubmitMyUserInfoResponse {
    private String flag;

    SubmitMyUserInfoResponse() {}

    public void setFlag(String flag)
    {
        this.flag = flag;
    }
}

@RestController
public class SubmitMyUserInfoController {
    @PostMapping("/SubmitMyUserInfo")
    public ResponseEntity<String> submitMyUserInfo(@RequestBody SubmitMyUserInfoRequest submitRequest) {
        int id = submitRequest.getUserId();
        String username = submitRequest.getUsername();
        String password = submitRequest.getPassword();
        System.out.println("[SubmitMyUserInfo]id: " + id);
        System.out.println("[SubmitMyUserInfo]username: " + username);
        System.out.println("[SubmitMyUserInfo]password: " + password);

        SubmitMyUserInfoResponse response = new SubmitMyUserInfoResponse();
        Business business = InformationBuffer.getBusiness(id);
        if (business != null)
        {
            short isManager = business.getUser().getPriority();
            int warehouseId = business.getUser().getWarehouseID();
            String res = business.updateUser(id, username, password, isManager, warehouseId);
            if (res.equals("更新用户信息成功"))
            {
                response.setFlag("1");
                business.getUser().setUserName(username);
                business.getUser().setPassword(password);
            }
            else
            {
                System.out.println("[SubmitMyUserInfo]: " + res);
                response.setFlag("0");
            }
        }
        else
        {
            System.out.println("[SubmitMyUserInfo]用户未登录");
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[SubmitMyUserInfo]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}