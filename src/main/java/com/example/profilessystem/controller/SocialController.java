package com.example.profilessystem.controller;

import com.example.profilessystem.auth.SessionKeys;
import com.example.profilessystem.entity.*;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.SocialService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/social")
public class SocialController {
    private final SocialService service;
    public SocialController(SocialService service){this.service=service;}
    private String me(HttpSession s){Object v=s.getAttribute(SessionKeys.USER);return v==null?null:v.toString();}
    @GetMapping("/friends") public Result<?> friends(HttpSession s){return service.friends(me(s));}
    @PostMapping("/friends/request") public Result<?> request(@RequestBody FriendRequestBody b,HttpSession s){return service.requestFriend(me(s),b.getUsername());}
    @PostMapping("/friends/{username}/{status}") public Result<?> update(@PathVariable String username,@PathVariable String status,HttpSession s){return service.updateFriend(me(s),username,status);}
    @GetMapping("/groups/{id}/messages") public Result<?> groupMessages(@PathVariable long id,HttpSession s){return service.groupMessages(id,me(s));}
    @GetMapping("/groups/{id}/members") public Result<?> groupMembers(@PathVariable long id,HttpSession s){return service.groupMembers(id,me(s));}
    @PostMapping("/groups/{id}/messages") public Result<?> groupSend(@PathVariable long id,@RequestBody ChatBody b,HttpSession s){return service.sendGroup(id,me(s),b.getContent());}
    @GetMapping("/direct/{username}") public Result<?> directMessages(@PathVariable String username,HttpSession s){return service.directMessages(me(s),username);}
    @PostMapping("/direct/{username}") public Result<?> directSend(@PathVariable String username,@RequestBody ChatBody b,HttpSession s){return service.sendDirect(me(s),username,b.getContent());}
}
