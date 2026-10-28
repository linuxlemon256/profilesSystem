package com.example.profilessystem.controller;

import com.example.profilessystem.auth.SessionKeys;
import com.example.profilessystem.entity.Manager;
import com.example.profilessystem.entity.ManagerLoginBody;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.ManagerService;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/login")
    public Result<String> login(@RequestBody ManagerLoginBody loginBody, HttpSession session) {
        Result<String> result = managerService.login(loginBody);
        if (result.getCode() == Result.CODE_SUCCESS) {
            session.setAttribute(SessionKeys.MANAGER, result.getData());
        }
        return result;
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.ok();
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
