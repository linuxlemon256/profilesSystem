package com.example.profilessystem.server;

import com.example.profilessystem.entity.Manager;
import com.example.profilessystem.mapper.ManagerMapper;
import org.springframework.stereotype.Service;

@Service
public class CRUDServer {
    private ManagerMapper managerMapper;
    public void managerInsert(Manager manager) {
        int row = managerMapper.insert(manager);
        if (row != 1) {
            return
        }
    }
}
