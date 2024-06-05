package com.example.software;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public String start() {
        // 假设这里进行了登录验证
        System.out.println("succeed");
        return "hello world!";
    }
}