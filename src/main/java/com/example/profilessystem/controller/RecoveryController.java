package com.example.profilessystem.controller;

import com.example.profilessystem.entity.*;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.RecoveryService;
import com.example.profilessystem.server.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recovery")
public class RecoveryController {
    private final RecoveryService recoveryService;
    private final UserService userService;
    public RecoveryController(RecoveryService recoveryService, UserService userService) { this.recoveryService = recoveryService; this.userService = userService; }

    @PostMapping("/invite")
    public Result<String> invite(@RequestBody InviteRecoveryBody body) { return recoveryService.requestInvite(body.getUsername(), body.getEmail(), body.getReason()); }
    @PostMapping("/password")
    public Result<String> password(@RequestBody PasswordRecoveryBody body) {
        if (body.getPassword() == null || !body.getPassword().equals(body.getConfirmPassword())) {
            return Result.fail("两次输入的新密码不一致");
        }
        return userService.resetPassword(body.getUsername(), body.getCode(), body.getPassword());
    }
    @PostMapping("/account")
    public Result<String> account(@RequestBody User body) { return userService.findAccountByEmail(body.getEmail()); }
    @GetMapping("/admin/pending")
    public Result<List<User>> pending() { return Result.ok(recoveryService.pending()); }
    @PostMapping("/admin/{id}/approve")
    public Result<String> approve(@PathVariable String id) { return recoveryService.approve(id); }
    @PostMapping("/admin/{id}/reject")
    public Result<String> reject(@PathVariable String id) { return recoveryService.reject(id); }
}
