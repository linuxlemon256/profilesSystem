package com.example.profilessystem.controller;
import com.example.profilessystem.server.ModerationService;
import com.example.profilessystem.result.Result;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.example.profilessystem.auth.SessionKeys;
@RestController
@RequestMapping("/api/moderation/admin/words")
public class ModerationController {
 private final ModerationService service;
 public ModerationController(ModerationService service){this.service=service;}
 public record AddWords(String words) {}
 @GetMapping public Result<?> all(HttpSession session){if(session.getAttribute(SessionKeys.MANAGER)==null)return Result.fail(401,"请先登录管理员账户");return Result.ok(service.words());}
 @PostMapping public Result<?> add(@RequestBody AddWords body,HttpSession session){if(session.getAttribute(SessionKeys.MANAGER)==null)return Result.fail(401,"请先登录管理员账户");return Result.ok(service.add(body.words()));}
 @DeleteMapping("/{id}") public Result<?> delete(@PathVariable long id,HttpSession session){if(session.getAttribute(SessionKeys.MANAGER)==null)return Result.fail(401,"请先登录管理员账户");return service.delete(id)>0?Result.ok():Result.fail(400,"词语不存在");}
}
