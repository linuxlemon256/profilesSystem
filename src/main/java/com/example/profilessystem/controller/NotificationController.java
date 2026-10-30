package com.example.profilessystem.controller;

import com.example.profilessystem.auth.SessionKeys;
import com.example.profilessystem.entity.Notification;
import com.example.profilessystem.entity.NotificationBody;
import com.example.profilessystem.mapper.NotificationMapper;
import com.example.profilessystem.mapper.UserMapper;
import com.example.profilessystem.result.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationMapper mapper; private final UserMapper users;
    public NotificationController(NotificationMapper mapper, UserMapper users){this.mapper=mapper;this.users=users;}
    private String user(HttpSession s){Object v=s.getAttribute(SessionKeys.USER);return v==null?null:v.toString();}
    private String manager(HttpSession s){Object v=s.getAttribute(SessionKeys.MANAGER);return v==null?"管理员":v.toString();}
    @GetMapping public Result<List<Notification>> mine(HttpSession s){String u=user(s);return u==null?Result.fail(Result.CODE_UNAUTHORIZED,"请先登录"):Result.ok(mapper.findByUser(u));}
    @PostMapping("/{id}/read") public Result<String> read(@PathVariable long id,HttpSession s){String u=user(s);return u==null?Result.fail(Result.CODE_UNAUTHORIZED,"请先登录"):mapper.markRead(id,u)==1?Result.ok(null):Result.fail("通知不存在");}
    @PostMapping("/admin/send") public Result<String> send(@RequestBody NotificationBody body,HttpSession s){
        if(body==null||body.getUsername()==null||body.getUsername().isBlank()||body.getContent()==null||body.getContent().isBlank())return Result.fail(Result.CODE_BAD_REQUEST,"账户名和通知内容不能为空");
        if(body.getContent().length()>2000)return Result.fail(Result.CODE_BAD_REQUEST,"通知不能超过 2000 字");
        if(users.findByName(body.getUsername().trim())==null)return Result.fail(Result.CODE_BAD_REQUEST,"用户不存在");
        Notification n=new Notification();n.setUsername(body.getUsername().trim());n.setSenderName(manager(s));n.setContent(body.getContent().trim());
        return mapper.insert(n)==1?Result.ok(null):Result.fail("发送失败");
    }
}
