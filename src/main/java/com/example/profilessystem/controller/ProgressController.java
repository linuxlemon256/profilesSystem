package com.example.profilessystem.controller;

import com.example.profilessystem.auth.SessionKeys;
import com.example.profilessystem.entity.ProgressBody;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.ProgressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping
    public Result<String> get(HttpSession session) {
        Object username = session.getAttribute(SessionKeys.USER);
        if (username == null) {
            return Result.fail(Result.CODE_UNAUTHORIZED, "请先登录");
        }
        return progressService.get(username.toString());
    }

    @PostMapping
    public Result<String> save(@RequestBody ProgressBody body, HttpSession session) {
        Object username = session.getAttribute(SessionKeys.USER);
        if (username == null) {
            return Result.fail(Result.CODE_UNAUTHORIZED, "请先登录");
        }
        return progressService.save(username.toString(), body.getData());
    }
}
