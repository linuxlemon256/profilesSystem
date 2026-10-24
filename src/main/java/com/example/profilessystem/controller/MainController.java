package com.example.profilessystem.controller;

import com.example.profilessystem.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/ping")
    public Result<Void> ping() {
        return Result.ok();
    }

}
