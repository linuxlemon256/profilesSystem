package com.example.profilessystem.controller;

import com.example.profilessystem.entity.Manager;
import com.example.profilessystem.mapper.ManagerMapper;
import com.example.profilessystem.result.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private ManagerMapper managerMapper;
    UserController(ManagerMapper managerMapper) {
        this.managerMapper = managerMapper;
    }
    @PostMapping("/manager/insert")
    public Result<String> insert(@RequestBody Manager manager) {
        managerMapper.insert(manager);
        return Result.ok("insert successfully");
    }
}
