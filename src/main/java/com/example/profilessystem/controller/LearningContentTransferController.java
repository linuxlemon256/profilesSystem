package com.example.profilessystem.controller;

import com.example.profilessystem.entity.LearningContentTransfer;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.LearningContentTransferService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/manager/content-transfer")
public class LearningContentTransferController {
    private final LearningContentTransferService service;

    public LearningContentTransferController(LearningContentTransferService service) {
        this.service = service;
    }

    @GetMapping("/overview")
    public Result<Map<String, Integer>> overview() {
        return Result.ok(service.overview());
    }

    @GetMapping("/export")
    public Result<LearningContentTransfer> exportContent() {
        return Result.ok(service.exportContent());
    }

    @PostMapping("/import")
    public Result<LearningContentTransfer.ImportSummary> importContent(@RequestBody LearningContentTransfer content) {
        return Result.ok(service.importContent(content));
    }
    @PostMapping("/replace")
    public Result<LearningContentTransfer.ImportSummary> replaceContent(
            @RequestBody LearningContentTransfer content,
            @RequestParam(defaultValue = "false") boolean confirmed,
            jakarta.servlet.http.HttpSession session) {
        if (session.getAttribute(com.example.profilessystem.auth.SessionKeys.MANAGER) == null)
            return Result.fail(401, "请先登录管理员账户");
        if (!confirmed) return Result.fail(400, "请先确认完整替换学习内容");
        return Result.ok(service.replaceContent(content));
    }
}
