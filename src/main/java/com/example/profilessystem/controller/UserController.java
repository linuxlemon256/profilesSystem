package com.example.profilessystem.controller;

import com.example.profilessystem.entity.User;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get/{name}")
    public Result<User> getUser(@PathVariable String name) {
        return Result.ok(userService.findByName(name));
    }
    @GetMapping("/get/all")
    public Result<List<User>> getAll() {
        return Result.ok(userService.findAll());
    }
    @GetMapping("/get/uncommit")
    public Result<List<User>> getUncommit() {
        return Result.ok(userService.findAllUncommit());
    }
    @GetMapping("/get/commit")
    public Result<List<User>> getCommit() {
        return Result.ok(userService.findAllCommit());
    }
    @PostMapping("/insert")
    public Result<User> insertUser(@RequestBody User user) {
        return Result.ok(userService.create(user));
    }
}
