package com.example.profilessystem.server;

import com.example.profilessystem.entity.User;
import com.example.profilessystem.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class UserService {

    private final UserMapper userMapper;

    UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Transactional
    public User create(User user) {
        int row = userMapper.insert(user);
        if (row != 1) {
            throw new IllegalStateException("插入失败");
        }
        return user;
    }
    @Transactional
    public User deleteByName(String name) {
        User user = userMapper.findByName(name);
        int row = userMapper.deleteByName(name);
        if (row != 1) {
            throw new IllegalStateException("删除失败");
        }
        return user;
    }
    @Transactional
    public User findByName(String name) {
        User user = userMapper.findByName(name);
        if (user == null) {
            throw new IllegalStateException("未找到该用户");
        }
        return user;
    }
    @Transactional
    public User update(String name) {
        User user = userMapper.findByName(name);
        if (user == null) {
            throw new IllegalStateException("未找到该用户");
        }
        user.setIsin(1);
        int row = userMapper.updateIsin(name);
        if (row != 1) {
            throw new IllegalStateException("添加失败");
        }
        return user;
    }
    @Transactional
    public ArrayList<User> findAllUncommit() {
        return userMapper.findByIsIn(0);
    }
    @Transactional
    public ArrayList<User> findAll() {
        return userMapper.findAll();
    }
    @Transactional
    public ArrayList<User> findAllCommit() {
        return userMapper.findByIsIn(1);
    }
}
