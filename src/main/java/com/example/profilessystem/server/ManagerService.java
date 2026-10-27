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
    public Manager create(Manager manager) {
        int rows = managerMapper.insert(manager);
        if (rows != 1) {
            throw new IllegalStateException("插入失败");
        }
        return manager;
    }
    /**
     * @param name
     * select
     * @return manager
     */
    @Transactional
    public Manager getManagerByName(String name) {
        Manager manager = managerMapper.findByName(name);
        if (manager == null) {
            throw new IllegalStateException("无用户");
        }
        return manager;
    }
    @Transactional
    public Manager getManagerById(int id) {
        Manager manager = managerMapper.findById(id);
        if (manager == null) {
            throw new IllegalStateException("无用户");
        }
        return manager;
    }
    /**
     *
     * @param password
     * update
     * @return massage
     */
    @Transactional
    public Manager update(int id, String newName, String password) {
        Manager manager = managerMapper.findById(id);
        if (manager == null) {
            throw new IllegalStateException("未查找到该用户");
        }
        String turePassword = manager.getPassword();
        if (!turePassword.equals(password)) {
            throw new IllegalStateException("密码错误");
        }
        int rows = managerMapper.updateNameById(id,newName);
        if (rows != 1) {
            throw new IllegalStateException("更新失败");
        }
        return manager;
    }
}
