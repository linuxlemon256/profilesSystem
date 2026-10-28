package com.example.profilessystem.server;

import com.example.profilessystem.entity.Manager;
import com.example.profilessystem.mapper.ManagerMapper;
import com.example.profilessystem.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagerService {

    private final ManagerMapper managerMapper;

    public ManagerService(ManagerMapper managerMapper) {
        this.managerMapper = managerMapper;
    }


    /**
     * @param manager
     * create
     * @return manager
     */
    @Transactional
    public Result<String> create(Manager manager) {
        int rows = managerMapper.insert(manager);
        if (rows != 1) {
            return Result.fail("插入失败");
        }
        return Result.ok(null);
    }
    /**
     * @param name
     * select
     * @return manager
     */
    @Transactional
    public Result<Manager> getManagerByName(String name) {
        Manager manager = managerMapper.findByName(name);
        if (manager == null) {
            return Result.fail("无用户");
        }
        return Result.ok(manager);
    }
    @Transactional
    public Result<String> getManagerById(int id) {
        Manager manager = managerMapper.findById(id);
        if (manager == null) {
            return Result.fail("无用户");
        }
        return Result.ok(null);
    }
    /**
     *
     * @param password
     * update
     * @return massage
     */
    @Transactional
    public Result<String> update(int id, String newName, String password) {
        Manager manager = managerMapper.findById(id);
        if (manager == null) {
            return Result.fail("未查找到该用户");
        }
        String turePassword = manager.getPassword();
        if (!turePassword.equals(password)) {
            return Result.fail("密码错误");
        }
        int rows = managerMapper.updateNameById(id,newName);
        if (rows != 1) {
            return Result.fail("更新失败");
        }
        return Result.ok(null);
    }
}
