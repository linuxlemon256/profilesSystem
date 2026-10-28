package com.example.profilessystem.server;

import com.example.profilessystem.mapper.ProgressMapper;
import com.example.profilessystem.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgressService {

    /** 单个用户的进度 JSON 上限，避免异常的超大内容。 */
    private static final int MAX_LENGTH = 1_000_000;

    private final ProgressMapper progressMapper;

    public ProgressService(ProgressMapper progressMapper) {
        this.progressMapper = progressMapper;
    }

    /** 读取当前用户的进度；从未保存过时 data 为 null。 */
    @Transactional(readOnly = true)
    public Result<String> get(String username) {
        String data = progressMapper.findDataByUsername(username);
        return Result.ok(data == null || data.isBlank() ? null : data);
    }

    @Transactional
    public Result<String> save(String username, String data) {
        if (data == null || data.isBlank()) {
            return Result.fail("进度内容为空");
        }
        if (data.length() > MAX_LENGTH) {
            return Result.fail("进度数据过大");
        }
        progressMapper.save(username, data);
        return Result.ok(null);
    }
}
