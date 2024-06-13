package com.example.software;

import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

class LoginRequest {
    private String username;
    private String password;

    // 构造函数
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
class LoginResponse {
    private String flag;
    private int userId = -1;
    String isManager = "";

    LoginResponse() {}

    void setFlag(String flag) {
        this.flag = flag;
    }
    void setUserId(int userId)
    {
        this.userId = userId;
    }
    void setIsManage(String isManager) {
        this.isManager = isManager;
    }
}

@RestController
public class LoginController {
    @Autowired
    private Business business;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println("[Login]username: " + username);
        System.out.println("[Login]password: " + password);

        LoginResponse loginResponse = new LoginResponse();
        // 登录验证
        if (/*!InformationBuffer.checkUser(username)*/false) // 用户已登录
        {
            loginResponse.setFlag("3");
        }
        else
        {
            String res = business.login(username, password);

            if (res.equals("用户不存在"))
            {
                System.out.println("[Login]: " + res);
                loginResponse.setFlag("2");
            }
            else if (res.equals("登录成功"))
            {
                System.out.println("[Login]: " + res);
                loginResponse.setFlag("1");
                loginResponse.setUserId(business.getUser().getUserId());
                String isManager = business.getUser().getPriority() == 1 ? "yes" : "no";
                loginResponse.setIsManage(isManager);
                if (InformationBuffer.checkUser(username))  // 用户未登录过
                    InformationBuffer.addBusiness(business);
            }
            else    // 密码错误
            {
                System.out.println("[Login]: " + res);
                loginResponse.setFlag("0");
            }
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(loginResponse);
        System.out.println("[Login]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}