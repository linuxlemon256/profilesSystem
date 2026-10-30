package com.example.profilessystem.server;

import com.example.profilessystem.entity.Group;
import com.example.profilessystem.entity.GroupBody;
import com.example.profilessystem.mapper.GroupMapper;
import com.example.profilessystem.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {
    private final GroupMapper mapper;
    public GroupService(GroupMapper mapper) { this.mapper = mapper; }

    @Transactional(readOnly = true)
    public Result<List<Group>> all() { return Result.ok(mapper.findAll()); }

    @Transactional(readOnly = true)
    public Result<Group> get(long id) {
        Group group = mapper.findById(id);
        if (group == null) return Result.fail("学习组不存在");
        group.setUsers(new java.util.ArrayList<>());
        for (String username : mapper.findMemberNames(id)) {
            com.example.profilessystem.entity.User user = new com.example.profilessystem.entity.User();
            user.setUsername(username);
            group.getUsers().add(user);
        }
        return Result.ok(group);
    }

    @Transactional
    public Result<Group> create(GroupBody body, String username) {
        if (body == null || body.getName() == null || body.getName().isBlank()) return Result.fail(Result.CODE_BAD_REQUEST, "学习组名称不能为空");
        if (body.getName().trim().length() > 80) return Result.fail(Result.CODE_BAD_REQUEST, "学习组名称不能超过 80 字");
        Group group = new Group();
        group.setName(body.getName().trim());
        group.setDescription(body.getDescription() == null ? "" : body.getDescription().trim());
        group.setOwnerUsername(username);
        if (mapper.insert(group) != 1 || mapper.addMember(group.getId(), username, "OWNER") != 1) return Result.fail("创建学习组失败");
        return Result.ok(group);
    }

    @Transactional
    public Result<String> join(long id, String username) {
        if (mapper.findById(id) == null) return Result.fail("学习组不存在");
        if (mapper.isMember(id, username) > 0) return Result.fail("你已经在学习组中");
        return mapper.addMember(id, username, "MEMBER") == 1 ? Result.ok(null) : Result.fail("加入失败");
    }

    @Transactional
    public Result<String> leave(long id, String username) {
        Group group = mapper.findById(id);
        if (group == null) return Result.fail("学习组不存在");
        if (username.equals(group.getOwnerUsername())) return Result.fail("创建者不能直接退出学习组");
        return mapper.removeMember(id, username) == 1 ? Result.ok(null) : Result.fail("你不在该学习组中");
    }
}
