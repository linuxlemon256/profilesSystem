package com.example.profilessystem.controller;

import com.example.profilessystem.auth.SessionKeys;
import com.example.profilessystem.entity.LoginBody;
import com.example.profilessystem.entity.RegisterBody;
import com.example.profilessystem.entity.User;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return userService.findByName(name);
    }
    @GetMapping("/get/all")
    public Result<List<User>> getAll() {
        return userService.findAll();
    }
    @GetMapping("/get/uncommit")
    public Result<List<User>> getUncommit() {
        return userService.findAllUncommit();
    }
    @GetMapping("/get/commit")
    public Result<List<User>> getCommit() {
        return userService.findAllCommit();
    }
    @GetMapping("/get/rejected")
    public Result<List<User>> getRejected() {
        return userService.findAllRejected();
    }
    @PostMapping("/insert")
    public Result<String> insertUser(@RequestBody User user) {
        return userService.create(user);
    }
    @PostMapping("/register")
    public Result<String> addUser(@RequestBody RegisterBody registerBody) {
        return userService.register(registerBody);
    }
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginBody loginBody, HttpSession session) {
        Result<String> result = userService.login(loginBody);
        if (result.getCode() == Result.CODE_SUCCESS) {
            session.setAttribute(SessionKeys.USER, result.getData());
        }
        return result;
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.ok();
    }
    @PostMapping("/approve/{name}")
    public Result<String> approve(@PathVariable String name) {
        return userService.update(name);
    }
    @PostMapping("/reject/{name}")
    public Result<String> reject(@PathVariable String name) {
        return userService.reject(name);
    }
}
