package com.example.software;

import com.example.software.database.serves.Business;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SoftwareApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoftwareApplication.class, args);
    }
}

@RestController
class ControllerTest {
    @GetMapping("/")
    public String start() {
        System.out.println("succeed");
        return "hello world!";
    }
}
