package com.example.profilessystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.profilessystem.mapper")
public class ProfilesSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfilesSystemApplication.class, args);
    }

}
