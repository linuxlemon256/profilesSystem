package com.example.profilessystem.controller;

import com.example.profilessystem.entity.CommunityRequest;
import com.example.profilessystem.result.Result;
import com.example.profilessystem.server.CommunityRequestService;
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
    public Result<String> submit(@RequestBody CommunityRequest request) {
        return service.submit(request);
    }

    @GetMapping("/admin/pending")
    public Result<List<CommunityRequest>> pending() { return service.pending(); }

    @GetMapping("/admin/all")
    public Result<List<CommunityRequest>> all() { return service.all(); }

    @PostMapping("/admin/{id}/status")
    public Result<String> updateStatus(@PathVariable long id, @RequestParam int status) {
        return service.updateStatus(id, status);
    }
}
