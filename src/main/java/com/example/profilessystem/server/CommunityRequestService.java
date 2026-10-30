package com.example.profilessystem.server;

import com.example.profilessystem.entity.CommunityRequest;
import com.example.profilessystem.mapper.CommunityRequestMapper;
import com.example.profilessystem.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class CommunityRequestService {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,}$");
    private final CommunityRequestMapper mapper;

    public CommunityRequestService(CommunityRequestMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional
    public Result<String> submit(CommunityRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()
                || request.getMessage() == null || request.getMessage().isBlank()
                || request.getContact() == null || request.getContact().isBlank()) {
            return Result.fail(Result.CODE_BAD_REQUEST, "请填写称呼、邮箱和具体内容");
        }
        if (!"feedback".equals(request.getRequestType()) && !"join".equals(request.getRequestType())) {
            return Result.fail(Result.CODE_BAD_REQUEST, "申请类型不正确");
        }
        request.setName(request.getName().trim());
        request.setContact(request.getContact().trim());
        if (!EMAIL_PATTERN.matcher(request.getContact()).matches()) {
            return Result.fail(Result.CODE_BAD_REQUEST, "请输入有效的邮箱地址");
        }
        request.setMessage(request.getMessage().trim());
        return mapper.insert(request) == 1 ? Result.ok(null) : Result.fail("提交失败");
    }

    @Transactional(readOnly = true)
    public Result<List<CommunityRequest>> pending() { return Result.ok(mapper.findPending()); }

    @Transactional(readOnly = true)
    public Result<List<CommunityRequest>> all() { return Result.ok(mapper.findAll()); }

    @Transactional
    public Result<String> updateStatus(long id, int status) {
        if (status < 1 || status > 2) return Result.fail(Result.CODE_BAD_REQUEST, "处理状态不正确");
        return mapper.updateStatus(id, status) == 1 ? Result.ok(null) : Result.fail("申请不存在或已处理");
    }
}
