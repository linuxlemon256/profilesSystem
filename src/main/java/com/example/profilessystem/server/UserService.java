package com.example.profilessystem.server;

import com.example.profilessystem.entity.RegisterBody;
import com.example.profilessystem.entity.User;
import com.example.profilessystem.mapper.UserMapper;
import com.example.profilessystem.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Transactional
    public Result<String> create(User user) {
        Result<User> existing = findByName(user.getUsername());
        if (existing.getCode() == Result.CODE_SUCCESS) {
            return Result.fail("用户名已存在");
        }
        int row = userMapper.insert(user);
        if (row != 1) {
            return Result.fail("插入失败");
        }
        return Result.ok(null);
    }
    @Transactional
    public Result<String> register(RegisterBody registerBody) {
        if (userMapper.findByName(registerBody.getUsername()) != null) {
            return Result.fail("用户名已存在");
        }
        if (userMapper.findByEmail(registerBody.getEmail()) != null && registerBody.getEmail() != null && !registerBody.getEmail().isBlank()) {
            return Result.fail("邮箱已被注册");
        }
        User user = new User(registerBody.getUsername(),null,"",null,0,registerBody.getEmail(),registerBody.getReason());
        int row = userMapper.insert(user);
        if (row != 1) {
            return Result.fail("插入失败");
        }
        return Result.ok(null);
    }
    @Transactional
    public Result<String> deleteByName(String name) {
        int row = userMapper.deleteByName(name);
        if (row != 1) {
            return Result.fail("删除失败");
        }
        return Result.ok(null);
    }
    @Transactional
    public Result<User> findByName(String name) {
        User user = userMapper.findByName(name);
        if (user == null) {
            return Result.fail("未找到该用户");
        }
        return Result.ok(user);
    }
    @Transactional
    public Result<String> update(String name) {
        User user = userMapper.findByName(name);
        if (user == null) {
            return Result.fail("未找到该用户");
        }
        user.setIsin(1);
        int row = userMapper.updateIsin(name);
        if (row != 1) {
            return Result.fail("添加失败");
        }
        return Result.ok(null);
    }
    @Transactional
    public Result<List<User>> findAllUncommit() {
        List<User> users = userMapper.findByIsIn(0);
        return Result.ok(users);
    }
    @Transactional
    public Result<List<User>> findAll() {
        List<User> users = userMapper.findAll();
        return Result.ok(users);
    }
    @Transactional
    public Result<List<User>> findAllCommit() {
        List<User> users = userMapper.findByIsIn(1);
        return Result.ok(users);
    }

}
