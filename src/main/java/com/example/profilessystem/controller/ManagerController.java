package com.example.profilessystem.controller;

import com.example.profilessystem.entity.Manager;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    private final ManagerService managerService;
    @Autowired
    ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }


    @PostMapping("/insert/")
    public Result<Manager> insert(@RequestBody Manager manager) {
        return Result.ok(managerService.create(manager));
    }

    @GetMapping("/get/{name}")
    public Result<Manager> get(@PathVariable String name) {
        return Result.ok(managerService.getManagerByName(name));
    }
}
