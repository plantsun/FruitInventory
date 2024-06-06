package com.example.software;

import com.google.gson.Gson;
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

    LoginResponse() {}

    void setFlag(String flag) {
        this.flag = flag;
    }
}

@RestController
public class LoginController {
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println("[Login]username: " + username);
        System.out.println("[Login]password: " + password);

        // 假设这里进行了登录验证
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setFlag("1");

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(loginResponse);
        System.out.println("[Login]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}