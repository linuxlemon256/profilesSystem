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
    public Result<String> insert(@RequestBody Manager manager) {
        return managerService.create(manager);
    }

    @GetMapping("/get/{name}")
    public Result<Manager> get(@PathVariable String name) {
        return managerService.getManagerByName(name);
    }
}
