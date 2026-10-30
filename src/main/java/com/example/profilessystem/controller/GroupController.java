package com.example.profilessystem.controller;

import com.example.profilessystem.auth.SessionKeys;
import com.example.profilessystem.entity.Group;
import com.example.profilessystem.entity.GroupBody;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.GroupService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService service;
    public GroupController(GroupService service) { this.service = service; }

    private String username(HttpSession session) { Object value = session.getAttribute(SessionKeys.USER); return value == null ? null : value.toString(); }

    @GetMapping
    public Result<List<Group>> all() { return service.all(); }

    @GetMapping("/{id}")
    public Result<Group> get(@PathVariable long id) { return service.get(id); }

    @PostMapping
    public Result<Group> create(@RequestBody GroupBody body, HttpSession session) {
        String user = username(session); if (user == null) return Result.fail(Result.CODE_UNAUTHORIZED, "请先登录");
        return service.create(body, user);
    }

    @PostMapping("/{id}/join")
    public Result<String> join(@PathVariable long id, HttpSession session) {
        String user = username(session); if (user == null) return Result.fail(Result.CODE_UNAUTHORIZED, "请先登录");
        return service.join(id, user);
    }

    @DeleteMapping("/{id}/leave")
    public Result<String> leave(@PathVariable long id, HttpSession session) {
        String user = username(session); if (user == null) return Result.fail(Result.CODE_UNAUTHORIZED, "请先登录");
        return service.leave(id, user);
    }
}
