package com.example.imagesearchformal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("com.example.imagesearchformal.mapper")
@SpringBootApplication
public class ImageSearchFormalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageSearchFormalApplication.class, args);
    }
}
