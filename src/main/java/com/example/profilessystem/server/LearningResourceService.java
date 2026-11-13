package com.example.profilessystem.server;

import com.example.profilessystem.entity.LearningResource;
import com.example.profilessystem.mapper.LearningResourceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningResourceService {
    private final LearningResourceMapper mapper;
    private final LearningAccessService accessService;
    private final org.springframework.jdbc.core.JdbcTemplate jdbc;

    public LearningResourceService(LearningResourceMapper mapper, LearningAccessService accessService, org.springframework.jdbc.core.JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.mapper = mapper;
        this.accessService = accessService;
    }

    public List<LearningResource> find(String category, String major, String query, String username) {
        return mapper.findVisible(clean(category), clean(major), clean(query), accessService.level(username));
    }

    public LearningResource findById(long id, String username) {
        return mapper.findVisibleById(id, accessService.level(username));
    }

    public List<LearningResource> findAllForAdmin() { return mapper.findAllForAdmin(); }

    public LearningResource create(LearningResource resource) {
        normalize(resource);
        applySafety(resource, null);
        if (mapper.insert(resource) != 1) throw new IllegalArgumentException("资源保存失败");
        return resource;
    }

    public boolean update(long id, LearningResource resource) {
        LearningResource previous = mapper.findByIdForAdmin(id);
        if (previous == null) return false;
        resource.setId(id);
        normalize(resource);
        applySafety(resource, previous);
        return mapper.update(resource) == 1;
    }

    public boolean delete(long id) { return mapper.delete(id) == 1; }

    private String clean(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }

    private void applySafety(LearningResource r, LearningResource previous) {
        String state = r.getSafetyStatus() == null ? "UNVERIFIED" : r.getSafetyStatus();
        if (!java.util.Set.of("UNVERIFIED","VERIFIED","RISK").contains(state)) throw new IllegalArgumentException("无效安全状态");
        boolean changed = previous != null && !java.util.Objects.equals(previous.getTargetUrl(),r.getTargetUrl());
        if (changed) { state="UNVERIFIED"; r.setReviewNote(null); }
        if ("RISK".equals(state)) {
            if (r.getReviewNote()==null || r.getReviewNote().isBlank()) throw new IllegalArgumentException("请填写风险原因");
            r.setEnabled(false);
        }
        r.setSafetyStatus(state);
        r.setReviewedAt("UNVERIFIED".equals(state) ? null : java.time.LocalDateTime.now());
        if (previous != null && !changed && state.equals(previous.getSafetyStatus()) && java.util.Objects.equals(r.getReviewNote(),previous.getReviewNote())) r.setReviewedAt(previous.getReviewedAt());
    }

    public static String validateTarget(String target) {
        if (target == null || target.isBlank() || target.length()>2048) throw new IllegalArgumentException("请填写有效链接，最长2048字符");
        target=target.trim();
        if (target.chars().anyMatch(c->Character.isISOControl(c)||Character.isWhitespace(c)) || target.contains("\\")) throw new IllegalArgumentException("链接不能包含空白或控制字符");
        try {
            var uri=new java.net.URI(target);
            if (uri.isAbsolute()) {
                if (!"https".equalsIgnoreCase(uri.getScheme()) || uri.getHost()==null || uri.getUserInfo()!=null) throw new IllegalArgumentException("外部链接仅支持不带账号密码的 HTTPS 地址");
            } else if (uri.getRawAuthority()!=null || !uri.getPath().matches("/?[A-Za-z0-9_-]+\\.html")) throw new IllegalArgumentException("站内地址须为本站 HTML 页面");
        } catch (java.net.URISyntaxException e) { throw new IllegalArgumentException("链接格式不正确"); }
        return target;
    }

    private void normalize(LearningResource resource) {
        if (resource == null) throw new IllegalArgumentException("请填写资源信息");
        if (resource.getTitle() == null || resource.getTitle().isBlank()) throw new IllegalArgumentException("资源标题不能为空");
        if (resource.getCategoryId() == null) throw new IllegalArgumentException("资源必须关联专业类别");
        resource.setTitle(resource.getTitle().trim());
        resource.setDescription(resource.getDescription() == null ? "" : resource.getDescription().trim());
        resource.setIcon(resource.getIcon() == null || resource.getIcon().isBlank() ? "✦" : resource.getIcon().trim());
        resource.setResourceType(resource.getResourceType() == null || resource.getResourceType().isBlank() ? "LINK" : resource.getResourceType().trim().toUpperCase());
        resource.setStatus(resource.getStatus() == null || resource.getStatus().isBlank() ? "DRAFT" : resource.getStatus().trim().toUpperCase());
        if (!java.util.Set.of("LINK","PAGE","FILE").contains(resource.getResourceType())) throw new IllegalArgumentException("无效资源类型");
        if (!java.util.Set.of("DRAFT","PUBLISHED").contains(resource.getStatus())) throw new IllegalArgumentException("无效发布状态");
        if (resource.getRequiredLevel()<0 || resource.getRequiredLevel()>2) throw new IllegalArgumentException("无效访问等级");
        if (resource.getTitle().length()>200 || resource.getDescription().length()>2000 || (resource.getReviewNote()!=null && resource.getReviewNote().length()>1000)) throw new IllegalArgumentException("标题、说明或核验备注超出长度限制");
        if (!"FILE".equals(resource.getResourceType())) resource.setTargetUrl(validateTarget(resource.getTargetUrl()));
        if (jdbc.queryForObject("SELECT COUNT(*) FROM major_category WHERE id=?",Integer.class,resource.getCategoryId())!=1) throw new IllegalArgumentException("专业类别不存在");
        if (resource.getMajorId()!=null && jdbc.queryForObject("SELECT COUNT(*) FROM major WHERE id=? AND category_id=?",Integer.class,resource.getMajorId(),resource.getCategoryId())!=1) throw new IllegalArgumentException("具体专业不属于当前类别");
        if (resource.getRouteId()!=null && jdbc.queryForObject("SELECT COUNT(*) FROM learning_route WHERE id=? AND category_id=? AND (major_id IS NULL OR major_id=?)",Integer.class,resource.getRouteId(),resource.getCategoryId(),resource.getMajorId())!=1) throw new IllegalArgumentException("路线不属于当前类别或具体专业");
    }
}
