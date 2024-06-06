package com.example.software;

import org.springframework.web.bind.annotation.*;

@RestController
public class ControllerTest {
    @GetMapping("/")
    public String start() {
        // 假设这里进行了登录验证
        System.out.println("succeed");
        return "hello world!";
    }
}