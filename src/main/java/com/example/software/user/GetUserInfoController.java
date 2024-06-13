package com.example.software.user;

import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

class User {
    private int userId;
    private String username;
    private String password;
    private String isManager;

    User(int userId, String username, String password, String isManager) {
        this.userId = userId;
        this.username = username;
        this.password  = password;
        this.isManager = isManager;
    }
}

class GetUserInfoResponse {
    private List<User> user;

    GetUserInfoResponse(List<User> user) {
        this.user = user;
    }
}

@RestController
public class GetUserInfoController {
    @Autowired
    private Business business;

    @GetMapping("/getUserInfo")
    public String getUserInfo() {
        List<User> userList = new ArrayList<>();
        for (com.example.software.database.table.User user : business.getAllUser())
        {
            int id = user.getUserId();
            String username = user.getUserName();
            String password = user.getPassword();
            String isManager = user.getPriority() == 1 ? "yes" : "no";
            userList.add(new User(id, username, password, isManager));
        }
        GetUserInfoResponse response = new GetUserInfoResponse(userList);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[GetUserInfo]Response: " + jsonResponse);
        return jsonResponse;
    }
}