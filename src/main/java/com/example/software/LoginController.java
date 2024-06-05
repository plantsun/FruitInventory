package com.example.software;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Information information) {
        // 假设这里进行了登录验证
        return ResponseEntity.ok("登录成功！");
    }
}

