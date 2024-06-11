package com.example.software;

import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

class RegisterRequest {
    private String username;
    private String password;

    // 构造函数
    public RegisterRequest(String username, String password) {
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
class RegisterResponse {
    private String flag;

    RegisterResponse() {}

    void setFlag(String flag) {
        this.flag = flag;
    }
}

@RestController
public class RegisterController {

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        System.out.println("[Register]username: " + username);
        System.out.println("[Register]password: " + password);

        RegisterResponse registerResponse = new RegisterResponse();
        // 注册逻辑
        Business business = new Business();
        String res = business.register(username, password);
        if (res.equals("该用户名已存在"))
        {
            registerResponse.setFlag("2");
        }
        else if (res.equals("注册成功"))
        {
            registerResponse.setFlag("1");
        }
        else    // 注册失败
        {
            registerResponse.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(registerResponse);
        System.out.println("[Register]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}