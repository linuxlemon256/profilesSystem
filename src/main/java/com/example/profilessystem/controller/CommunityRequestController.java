package com.example.profilessystem.controller;

import com.example.profilessystem.entity.CommunityRequest;
import com.example.profilessystem.entity.CommunityMessageBody;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.CommunityRequestService;
import com.example.profilessystem.auth.SessionKeys;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
public class CommunityRequestController {
    private final CommunityRequestService service;

    public CommunityRequestController(CommunityRequestService service) {
        this.service = service;
    }

    @PostMapping("/request")
    public Result<String> submit(@RequestBody CommunityRequest request, HttpSession session) {
        Object username = session.getAttribute(SessionKeys.USER);
        if (username != null) request.setUsername(username.toString());
        return service.submit(request);
    }

    @GetMapping("/messages")
    public Result<List<com.example.profilessystem.entity.CommunityMessage>> messages(HttpSession session) {
        Object username = session.getAttribute(SessionKeys.USER);
        if (username == null) return Result.fail(Result.CODE_UNAUTHORIZED, "请先登录");
        return service.messagesForUser(username.toString());
    }

    @PostMapping("/{id}/messages")
    public Result<String> send(@PathVariable long id, @RequestBody CommunityMessageBody body, HttpSession session) {
        Object username = session.getAttribute(SessionKeys.USER);
        if (username == null) return Result.fail(Result.CODE_UNAUTHORIZED, "请先登录");
        return service.replyAsUser(id, username.toString(), body.getContent());
    }

    @GetMapping("/admin/pending")
    public Result<List<CommunityRequest>> pending() { return service.pending(); }

    @GetMapping("/admin/all")
    public Result<List<CommunityRequest>> all() { return service.all(); }

    @PostMapping("/admin/{id}/status")
    public Result<String> updateStatus(@PathVariable long id, @RequestParam int status) {
        return service.updateStatus(id, status);
    }

    @GetMapping("/admin/{id}/messages")
    public Result<List<com.example.profilessystem.entity.CommunityMessage>> adminMessages(@PathVariable long id) {
        return service.messagesForRequest(id);
    }

    @PostMapping("/admin/{id}/reply")
    public Result<String> adminReply(@PathVariable long id, @RequestBody CommunityMessageBody body) {
        return service.replyAsAdmin(id, body.getContent());
    }
}
