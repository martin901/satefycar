package com.example.demo;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableSwagger
public class SafetyCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafetyCarApplication.class, args);
    }

}
