package com.example.profilessystem.server;

import com.example.profilessystem.entity.Manager;
import com.example.profilessystem.entity.ManagerLoginBody;
import com.example.profilessystem.mapper.ManagerMapper;
import com.example.profilessystem.result.Result;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagerService {

    private final ManagerMapper managerMapper;
    private final PasswordEncoder passwordEncoder;

    public ManagerService(ManagerMapper managerMapper, PasswordEncoder passwordEncoder) {
        this.managerMapper = managerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /** 首次初始化状态：管理员表为空时才允许初始化。 */
    @Transactional(readOnly = true)
    public boolean needsBootstrap() {
        return managerMapper.count() == 0;
    }

    /** 创建首个管理员；管理员表一旦有记录，该接口永久失效。 */
    @Transactional
    public Result<String> bootstrap(Manager manager) {
        if (!needsBootstrap()) {
            return Result.fail("管理员初始化已完成");
        }
        if (manager == null || manager.getManagername() == null
                || manager.getManagername().trim().isEmpty()
                || manager.getPassword() == null || manager.getPassword().length() < 6) {
            return Result.fail("管理员账号不能为空，密码至少需要 6 位");
        }
        manager.setManagername(manager.getManagername().trim());
        manager.setLevel(1);
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        return managerMapper.insertFirst(manager) == 1
                ? Result.ok(null) : Result.fail("初始化失败");
    }


    /**
     * @param manager
     * create
     * @return manager
     */
    @Transactional
    public Result<String> create(Manager manager) {
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        int rows = managerMapper.insert(manager);
        if (rows != 1) {
            return Result.fail("插入失败");
        }
        return Result.ok(null);
    }
    /** 管理员登录：账号 + 密码，密码用 BCrypt 校验。 */
    @Transactional
    public Result<String> login(ManagerLoginBody loginBody) {
        String name = loginBody.getManagername() == null ? "" : loginBody.getManagername().trim();
        String password = loginBody.getPassword() == null ? "" : loginBody.getPassword();
        if (name.isEmpty() || password.isEmpty()) {
            return Result.fail("请填写账号和密码");
        }
        Manager manager = managerMapper.findByName(name);
        if (manager == null || !passwordEncoder.matches(password, manager.getPassword())) {
            return Result.fail("账号或密码错误");
        }
        return Result.ok(manager.getName());
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
        if (!passwordEncoder.matches(password, manager.getPassword())) {
            return Result.fail("密码错误");
        }
        int rows = managerMapper.updateNameById(id,newName);
        if (rows != 1) {
            return Result.fail("更新失败");
        }
        return Result.ok(null);
    }
}
