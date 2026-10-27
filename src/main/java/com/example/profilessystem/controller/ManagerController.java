package com.example.profilessystem.controller;

import com.example.profilessystem.entity.Manager;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.ManagerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    private ManagerService managerService;

    ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }


    @PostMapping("/insert")
    public Result<Void> insert(@RequestBody Manager manager) {
        return Result.ok();
    }

    @GetMapping("/get/{name}")
    public Result<Manager> get(@PathVariable("name") String name) {
        return Result.ok(managerService.getManagerByName(name));
    }
}
