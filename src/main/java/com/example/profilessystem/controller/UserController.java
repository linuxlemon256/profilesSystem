package com.example.profilessystem.controller;

import com.example.profilessystem.auth.SessionKeys;
import com.example.profilessystem.entity.LoginBody;
import com.example.profilessystem.entity.RegisterBody;
import com.example.profilessystem.entity.User;
import com.example.profilessystem.entity.UserAdminUpdateBody;
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

    @PostMapping("/login/password")
    public Result<String> loginByPassword(@RequestBody LoginBody loginBody, HttpSession session) {
        Result<String> result = userService.loginByPassword(loginBody);
        if (result.getCode() == Result.CODE_SUCCESS) session.setAttribute(SessionKeys.USER, result.getData());
        return result;
    }

    @PostMapping("/login/email")
    public Result<String> loginByEmail(@RequestBody LoginBody loginBody, HttpSession session) {
        Result<String> result = userService.loginByEmail(loginBody);
        if (result.getCode() == Result.CODE_SUCCESS) session.setAttribute(SessionKeys.USER, result.getData());
        return result;
    }


    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.ok();
    }

    /** 当前登录用户的账户资料，供账户主页使用。 */
    @GetMapping("/me")
    public Result<User> me(HttpSession session) {
        Object username = session.getAttribute(SessionKeys.USER);
        if (username == null) {
            return Result.fail(Result.CODE_UNAUTHORIZED, "请先登录");
        }
        return userService.findByName(username.toString());
    }

    /** 当前登录用户的数字等级：0 游客，1 关联人员，2 内部人员。 */
    @GetMapping("/level")
    public Result<Integer> level(HttpSession session) {
        Object username = session.getAttribute(SessionKeys.USER);
        if (username == null) {
            return Result.fail(Result.CODE_UNAUTHORIZED, "请先登录");
        }
        Result<User> userResult = userService.findByName(username.toString());
        if (userResult.getCode() != Result.CODE_SUCCESS || userResult.getData() == null) {
            return Result.fail(Result.CODE_UNAUTHORIZED, "账户不存在或已失效");
        }
        return Result.ok(userResult.getData().getLevel());
    }
    @PostMapping("/approve/{name}")
    public Result<String> approve(@PathVariable String name) {
        return userService.update(name);
    }
    @PostMapping("/reject/{name}")
    public Result<String> reject(@PathVariable String name) {
        return userService.reject(name);
    }

    @PutMapping("/admin/update/{name}")
    public Result<String> updateByAdmin(@PathVariable String name, @RequestBody UserAdminUpdateBody body) {
        return userService.updateByAdmin(name, body);
    }
}
