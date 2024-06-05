package com.example.software;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Information registrationRequest) {
        // 处理注册逻辑

        // 假设注册成功，返回成功响应
        return ResponseEntity.ok("Registration successful!");
    }
}